package com.strelizia.arknights.controller;

import com.strelizia.arknights.model.MessageInfo;
import com.strelizia.arknights.service.SeTuService;
import com.strelizia.arknights.util.SendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangzy
 * @Date 2020/12/24 15:58
 * 私聊回复接口单独实现，尽量不与群聊接口重复
 **/
@RequestMapping("private")
@RestController
@Slf4j
public class PrivateChatController {

    @Autowired
    private SeTuService seTuService;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @PostMapping("chat")
    public String receive(
            @RequestBody MessageInfo message
    ){
        Long qq = message.getQq();
        log.info("接受到私聊消息:{}",message.getText());
        String text = message.getText();
        String result = null;
        //发送的是图片才触发
        if (text.startsWith("{")) {
            JSONObject jsonObj = new JSONObject(text);
            String content = jsonObj.getString("Content");
            //不带文字的纯图片触发保存涩图
            if (content.equals("") || content == null) {
                result = seTuService.getImageIntoDb(text, 1, "你");
                sendMsgUtil.CallOPQApiSendMsg(qq, result, 1);
            }
        }
        return result;
    }
}
