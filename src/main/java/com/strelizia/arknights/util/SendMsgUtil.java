package com.strelizia.arknights.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strelizia.arknights.model.SendMsgRespInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author wangzy
 * @Date 2020/12/11 9:37
 **/
@Slf4j
public class SendMsgUtil {

    @Value("${loginQq}")
    private static Long loginQq;

    private static final String sendTextMsgUrl = "http://8.131.244.232:8888/v1/LuaApiCaller?qq="
            + loginQq + "&funcname=SendMsg";
    //http://8.131.244.232:8888/v1/LuaApiCaller?qq=3022645754&funcname=SendMsg
    /**
     * {
     *     "toUser": 830405854,
     *     "sendToType": 2,
     *     "sendMsgType": "TextMsg",
     *     "content": "机器人测试消息",
     *     "group": 0
     * }
     */
    public static SendMsgRespInfo sendTextMsgToGroup(RestTemplate restTemplate, Long groupId, String Text) {
        Map<String,Object> map = new HashMap<>(7);
        map.put("toUser",groupId);
        map.put("sendToType",2);
        map.put("sendMsgType","TextMsg");
        map.put("content",Text);
        map.put("group",0);

        String jsonData = null;
        try {
            jsonData = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("封装请求Body失败", e.getMessage());
        }
        //获取请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonData, httpHeaders);
        //发送请求，封装结果数据
            SendMsgRespInfo body = restTemplate
                    .postForEntity(sendTextMsgUrl, httpEntity, SendMsgRespInfo.class).getBody();
        return body;
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(sendTextMsgUrl);
        SendMsgRespInfo test = sendTextMsgToGroup(restTemplate, 901158551L, "测试");
        System.out.println(test.getRet());
    }
}
