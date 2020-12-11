package com.strelizia.arknights.controller;

import com.strelizia.arknights.model.MessageInfo;
import com.strelizia.arknights.service.AgentService;
import com.strelizia.arknights.util.SendMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    public String receive(
            @RequestBody MessageInfo message
    ){
        Long qq = message.getQq();
        String name = message.getName();
        String[] s = message.getText().split(" ");
        if (s.length==1){
            s = new String[]{s[0], null};
        }
        Long groupId = message.getGroupId();
        switch (s[0]){
            case "十连":
                String shiLian = agentService.shiLian(s[1], qq, name,groupId);
                return shiLian;
            case "抽卡":
                String chouKa = agentService.chouKa(s[1],qq,name,groupId);
                return chouKa;
            case "卡池":
                String kaChi = agentService.selectPool(groupId);
                return kaChi;
            case "垫刀查询":
                String dianDao = agentService.selectFoundCount(qq,name,groupId);
                return dianDao;
            default:
                String dontKnow = "俺不晓得你在锁啥子";
                return dontKnow;
        }
    }
}
