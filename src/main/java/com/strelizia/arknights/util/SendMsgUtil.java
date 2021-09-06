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
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static final String picUrl = "PicUrl";

    public static final String picBase64Buf = "PicBase64Buf";

    @Resource(name = "taskModuleExecutor")
    @Autowired
    private ThreadPoolTaskExecutor poolTaskExecutor;

    @Autowired
    protected RestTemplate restTemplate;

    private final String sendTextMsgApi = "/v1/LuaApiCaller";

    private void sendTextMsgToGroup(RestTemplate restTemplate, Long groupId, String Text, String sendTextMsgUrl, Integer sendToType) {
        Map<String, Object> map = new HashMap<>(7);
        map.put("ToUserUid", groupId);
        map.put("SendToType", sendToType);
        map.put("SendMsgType", "TextMsg");
        map.put("Content", Text);

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
        restTemplate.postForEntity(sendTextMsgUrl, httpEntity, SendMsgRespInfo.class);
    }

    private void sendTextImgToGroup(RestTemplate restTemplate, Long groupId, String Text, String picType, String url, String sendTextMsgUrl, Integer sendToType) {
        Map<String, Object> map = new HashMap<>(7);
        map.put("ToUserUid", groupId);
        map.put("SendToType", sendToType);
        map.put("SendMsgType", "PicMsg");
        map.put("Content", Text);
        map.put(picType, url);

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
        restTemplate.postForEntity(sendTextMsgUrl, httpEntity, SendMsgRespInfo.class);
    }

    public void CallOPQApiSendMsg(Long groupId, String s, Integer sendToType) {
        poolTaskExecutor.execute(() -> {
            try {
                String atUser = null;
                Pattern pattern = Pattern.compile("\\[ATUSER\\([0-9]*\\)]");
                String str = s;
                Matcher matcher = pattern.matcher(str);
                if (matcher.find())
                {
                    atUser = matcher.group();
                    str = matcher.replaceAll("");
                }
                Text text = new Text(str);
                if (text.getMaxRow().length() > 15 && text.getRowsNum() > 5) {
                    //文字太长就发图片
                    if (text.getRowsNum() > 7) {
                        sendTextImgToGroup(restTemplate, groupId, atUser, SendMsgUtil.picBase64Buf, TextToImage.createImage(str, new Font("楷体", Font.PLAIN, 50)),
                                "http://" + OPQUrl + ":8888" + sendTextMsgApi + "?qq=" + loginQq + "&funcname=SendMsgV2", sendToType);
                    } else {
                        sendTextImgToGroup(restTemplate, groupId, atUser, SendMsgUtil.picBase64Buf, TextToImage.createImage(str, new Font("楷体", Font.PLAIN, 100)),
                                "http://" + OPQUrl + ":8888" + sendTextMsgApi + "?qq=" + loginQq + "&funcname=SendMsgV2", sendToType);
                    }
                } else {
                    sendTextMsgToGroup(restTemplate, groupId, str,
                            "http://" + OPQUrl + ":8888" + sendTextMsgApi + "?qq=" + loginQq + "&funcname=SendMsgV2", sendToType);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        log.info("发送消息{}成功", s);
    }

    /**
     * 不调用文字转图片，纯文字发送的方法
     */
//    public void CallOPQApiSendMsg(Long groupId, String s, Integer sendToType){
//        poolTaskExecutor.execute(() -> sendTextMsgToGroup(restTemplate, groupId, s,
//                "http://" + OPQUrl + ":8888" + sendTextMsgApi + "?qq=" +  loginQq + "&funcname=SendMsg",sendToType));
//        log.info("发送消息{}成功",s);
//    }
    public void CallOPQApiSendImg(Long groupId, String s, String picType, String imgUrl, Integer sendToType) {
        poolTaskExecutor.execute(() -> sendTextImgToGroup(restTemplate, groupId, s, picType, imgUrl,
                "http://" + OPQUrl + ":8888" + sendTextMsgApi + "?qq=" + loginQq + "&funcname=SendMsgV2", sendToType));
        log.info("发送消息图片+文字{}成功", s);
    }

    public void CallOPQApiSendMyself(String s) {
        poolTaskExecutor.execute(() -> sendTextMsgToGroup(restTemplate, loginQq, s,
                "http://" + OPQUrl + ":8888" + sendTextMsgApi + "?qq=" + loginQq + "&funcname=SendMsgV2", 1));
    }
}

