package com.wzy.arknights.controller;

import com.wzy.arknights.model.FoundInfo;
import com.wzy.arknights.service.AgentService;
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

    @GetMapping("getPoolName")
    public String getPoolName(){
        return agentService.selectPool();
    }

    @PostMapping("chouKa")
    public String chouKa(
            @RequestBody FoundInfo foundInfo
    ){
        return agentService.chouKa(foundInfo.getPool(),foundInfo.getQq(),foundInfo.getName());
    }

    @PostMapping("shiLian")
    public String shiLian(
            @RequestBody FoundInfo foundInfo){
        return agentService.shiLian(foundInfo.getPool(),foundInfo.getQq(),foundInfo.getName());
    }
}
