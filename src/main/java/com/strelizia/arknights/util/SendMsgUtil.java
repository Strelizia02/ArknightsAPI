package com.strelizia.arknights.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strelizia.arknights.model.SendMsgRespInfo;
import com.strelizia.arknights.model.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.util.*;

/**
 * @author wangzy
 * @Date 2020/12/11 9:37
 **/
@Slf4j
@Component
public class SendMsgUtil {

    @Value("${userConfig.loginQq}")
    private Long loginQq;

    @Value("${userConfig.OPQUrl}")
    private String OPQUrl;

    public static final String picUrl = "picUrl";

    public static final String picBase64Buf = "picBase64Buf";

    @Autowired
    private ThreadPoolTaskExecutor poolTaskExecutor;

    @Autowired
    protected RestTemplate restTemplate;

    private final String sendTextMsgApi = "/v1/LuaApiCaller";

    public void sendTextMsgToGroup(RestTemplate restTemplate, Long groupId, String Text, String sendTextMsgUrl, Integer sendToType) {
        Map<String,Object> map = new HashMap<>(7);
        map.put("toUser",groupId);
        map.put("sendToType",sendToType);
        map.put("sendMsgType","TextMsg");
        map.put("content",Text);
        map.put("group",0);

        String jsonData = null;
        try {
            jsonData = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("封装请求Body失败{}", e.getMessage());
        }
        //获取请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8;OAuth e1bac1205283429d818c5ab6ae4c2b10");
        httpHeaders.setContentType(type);
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonData, httpHeaders);
        //发送请求，封装结果数据
        restTemplate.postForEntity(sendTextMsgUrl, httpEntity, SendMsgRespInfo.class).getBody();
    }

    public void sendTextImgToGroup(RestTemplate restTemplate, Long groupId, String Text, String picType, String url, String sendTextMsgUrl, Integer sendToType) {
        Map<String,Object> map = new HashMap<>(7);
        map.put("toUser",groupId);
        map.put("sendToType",sendToType);
        map.put("sendMsgType","PicMsg");
        map.put("content",Text);
        map.put("group",0);
        map.put(picType,url);

        String jsonData = null;
        try {
            jsonData = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("封装请求Body失败{}", e.getMessage());
        }
        //获取请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        httpHeaders.setContentType(type);
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonData, httpHeaders);
        //发送请求，封装结果数据
        restTemplate.postForEntity(sendTextMsgUrl, httpEntity, SendMsgRespInfo.class).getBody();
    }

    public void CallOPQApiSendMsg(Long groupId, String s, Integer sendToType){
        poolTaskExecutor.execute(() -> {
            try {
                Text text = new Text(s);
                if (text.getMaxRow().length() > 12 && text.getRowsNum() > 5) {
                    //文字太长就发图片
                    sendTextImgToGroup(restTemplate, groupId, null, SendMsgUtil.picBase64Buf, TextToImage.createImage(s, new Font("楷体", Font.PLAIN, 24)),
                            "http://" + OPQUrl + ":8888" + sendTextMsgApi + "?qq=" +  loginQq + "&funcname=SendMsg",sendToType);
                } else {
                    sendTextMsgToGroup(restTemplate, groupId, s,
                            "http://" + OPQUrl + ":8888" + sendTextMsgApi + "?qq=" + loginQq + "&funcname=SendMsg", sendToType);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            });
        log.info("发送消息{}成功",s);
    }

    public void CallOPQApiSendImg(Long groupId, String s, String picType, String imgUrl ,Integer sendToType){
        poolTaskExecutor.execute(() -> sendTextImgToGroup(restTemplate, groupId, s, picType, imgUrl,
                "http://" + OPQUrl + ":8888" + sendTextMsgApi + "?qq=" +  loginQq + "&funcname=SendMsg",sendToType));
        log.info("发送消息图片+文字{}成功",s);
    }

}
