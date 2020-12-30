package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.AdminUserMapper;
import com.strelizia.arknights.dao.SeTuMapper;
import com.strelizia.arknights.model.AdminUserInfo;
import com.strelizia.arknights.model.ImgUrlInfo;
import com.strelizia.arknights.service.SeTuService;
import com.strelizia.arknights.util.AdminUtil;
import com.strelizia.arknights.util.ImageUtil;
import com.strelizia.arknights.util.SendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * @author wangzy
 * @Date 2020/12/23 16:53
 **/

@Service
@Slf4j
public class SeTuServiceImpl implements SeTuService {

    @Autowired
    private SeTuMapper seTuMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Resource(name="taskModuleExecutor")
    @Autowired
    private ThreadPoolTaskExecutor poolTaskExecutor;

    @Autowired
    private ImageUtil imageUtil;

    @Value("${pixiv.count}")
    private Integer count;

    @Override
    public String getImageIntoDb(String json, Integer type, String name) {
        //获取json
        JSONObject jsonObj = new JSONObject(json);
        //解析里面图片url
        JSONArray array = new JSONArray(jsonObj.get("GroupPic").toString());
        String url = array.getJSONObject(0).getString("Url");
        //开一个线程url转换为base64
        poolTaskExecutor.execute(() -> {
            String base64 = imageUtil.getImageBase64ByUrl(url);
            seTuMapper.insertSeTuUrl(base64, type);
        });
        return "感谢[" + name + "]的涩图";
    }

    @Override
    public String PrivategetImageIntoDb(String json, Integer type) {
        //私聊信息涩图保存，逻辑同上
        JSONObject jsonObj = new JSONObject(json);
        JSONArray array = new JSONArray(jsonObj.get("FriendPic").toString());
        String url = array.getJSONObject(0).getString("Url");
        poolTaskExecutor.execute(() -> {
            String base64 = imageUtil.getImageBase64ByUrl(url);
            seTuMapper.insertSeTuUrl(base64, type);
        });
        return "涩图已收到get√";
    }

    @Override
    public String sendImageByType(Long qq, Long groupId, Integer type, String name) {
        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
        //获取，某人的今日涩图数
        Integer pixiv = seTuMapper.selectTodaySeTuByQQ(qqMd5);
        //新人玩涩图从0开始
        if (pixiv == null){
            pixiv = 0;
        }
        //查询管理员列表
        List<AdminUserInfo> admins = adminUserMapper.selectAllAdmin();
        //判断是否有无限涩图权限
        boolean b = AdminUtil.getImgAdmin(qqMd5,admins);
        //只有在有无限涩图权限或者没有达到今日涩图上限的时候才发送涩图
        if (pixiv < count || b) {
            ImgUrlInfo img = seTuMapper.selectOneSeTuUrl(type);
            if (img == null){
                return "没有找到涩图哦,可以发送[##给你涩图 图片]尝试上传一张涩图";
            }else {
                String url = img.getUrl();
                log.info("发送编号为{}的涩图",img.getId());
                sendMsgUtil.CallOPQApiSendImg(groupId, null,SendMsgUtil.picBase64Buf, url,2);
                //更新请求涩图数量
                seTuMapper.updateTodaySeTu(qqMd5,name,groupId);
                //空字符串不返回文字信息
                return "";
            }
        }else {
            return name + "别冲了，一天就"+ count +"张涩图";
        }
    }

    /**
     * 涩图输出到指定目录
     * @param dir
     * @return
     */
    @Override
    public Integer getAllImageIntoLocal(String dir) {
        List<ImgUrlInfo> imgUrlInfos = seTuMapper.selectAllSeTuUrl(1);
        int i = 0;
        for (ImgUrlInfo img:imgUrlInfos) {
            imageUtil.getImgToLocal(dir, img.getId(),img.getUrl());
            i++;
        }
        return i;
    }

}
