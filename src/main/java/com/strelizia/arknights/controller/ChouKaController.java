package com.strelizia.arknights.controller;

import com.strelizia.arknights.model.FoundInfo;
import com.strelizia.arknights.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangzy
 * @Date 2020/11/20 17:02
 **/
@RequestMapping("ChouKa")
@RestController
public class ChouKaController {
    @Autowired
    private AgentService agentService;

    //获取卡池列表
    @GetMapping("getPoolName")
    public String getPoolName(){
        return agentService.selectPool();
    }

    //返回单抽结果
    @PostMapping("chouKa")
    public String chouKa(
            @RequestBody FoundInfo foundInfo
    ){
        return agentService.chouKa(foundInfo.getPool(),foundInfo.getQq(),foundInfo.getName());
    }

    //返回十连结果
    @PostMapping("shiLian")
    public String shiLian(
            @RequestBody FoundInfo foundInfo){
        return agentService.shiLian(foundInfo.getPool(),foundInfo.getQq(),foundInfo.getName());
    }
}
