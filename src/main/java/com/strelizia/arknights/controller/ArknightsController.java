package com.strelizia.arknights.controller;

import com.strelizia.arknights.model.MessageInfo;
import com.strelizia.arknights.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangzy
 * @Date 2020/11/20 17:02
 **/
@RequestMapping("Arknights")
@RestController
public class ArknightsController {
    @Autowired
    private AgentService agentService;

    //返回单抽结果
    @PostMapping("receive")
    public String chouKa(
            @RequestBody MessageInfo message
    ){

        Long qq = message.getQq();
        String name = message.getName();
        String[] s = message.getText().split(" ");
        switch (s[0]){
            case "十连":
                return agentService.shiLian(s[0],qq,name);
            case "抽卡":
                return agentService.chouKa(s[0],qq,name);
            case "卡池":
                return agentService.selectPool();
            default:
                return "俺不晓得你在锁啥子";
        }
    }
}
