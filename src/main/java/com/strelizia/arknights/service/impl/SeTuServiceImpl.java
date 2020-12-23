package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.SeTuMapper;
import com.strelizia.arknights.service.SeTuService;
import com.strelizia.arknights.util.SendMsgUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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

    @Value("${pixiv.count}")
    private Integer count;

    @Override
    public String getImageIntoDb(String json, Integer type, String name) {
        JSONObject jsonObj = new JSONObject(json);
        JSONArray array = new JSONArray(jsonObj.get("GroupPic").toString());
        String url = array.getJSONObject(0).getString("Url");
        Integer i = seTuMapper.insertSeTuUrl(url, type);
        if (i == 0)
        {
            return "涩图保存失败";
        }
        return "感谢[" + name + "]的涩图";
    }

    @Override
    public String sendImageByType(Long qq, Long groupId, Integer type, String name) {
        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
        Integer pixiv = seTuMapper.selectTodaySeTuByQQ(qqMd5);
        if (pixiv < count) {
            List<String> urls = seTuMapper.selectSeTuUrl(type);
            int i = new Random().nextInt();
            sendMsgUtil.CallOPQApiSendImg(groupId,null,urls.get(i));
            return "";
        }else {
            return name + "别冲了，一天就"+ count +"张涩图";
        }
    }
}
