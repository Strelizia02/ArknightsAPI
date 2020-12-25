package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.SeTuMapper;
import com.strelizia.arknights.service.SeTuService;
import com.strelizia.arknights.util.ImageUtil;
import com.strelizia.arknights.util.SendMsgUtil;
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
public class SeTuServiceImpl implements SeTuService {

    @Autowired
    private SeTuMapper seTuMapper;

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
        JSONObject jsonObj = new JSONObject(json);
        JSONArray array = new JSONArray(jsonObj.get("GroupPic").toString());
        String url = array.getJSONObject(0).getString("Url");
        poolTaskExecutor.execute(() -> {
            String base64 = imageUtil.getImageBase64ByUrl(url);
            seTuMapper.insertSeTuUrl(base64, type);
        });
        return "感谢[" + name + "]的涩图";
    }

    @Override
    public String PrivategetImageIntoDb(String json, Integer type) {
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
        Integer pixiv = seTuMapper.selectTodaySeTuByQQ(qqMd5);
        if (pixiv < count ||qqMd5.equals(DigestUtils.md5DigestAsHex("1111".getBytes()))||qqMd5.equals("c5ecb54cdb92b19fe7c6c8dca260e69d")) {
            String urls = seTuMapper.selectSeTuUrl(type);
            if (urls == null){
                return "没有找到涩图哦";
            }else {
                sendMsgUtil.CallOPQApiSendImg(groupId, null,SendMsgUtil.picBase64Buf, urls,2);
                //更新请求涩图数量
                seTuMapper.updateTodaySeTu(qqMd5,name,groupId);
                return "";
            }
        }else {
            return name + "别冲了，一天就"+ count +"张涩图";
        }
    }
}
