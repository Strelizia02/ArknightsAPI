package com.wzy.arknights.controller;

import com.wzy.arknights.model.AccountInfo;
import com.wzy.arknights.model.FoundInfo;
import com.wzy.arknights.service.AgentService;
import com.wzy.arknights.service.ArknightsService;
import com.wzy.arknights.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/11/20 17:02
 **/
@RequestMapping("Arknights")
@RestController
public class ArknightsController {
    @Autowired
    private ArknightsService arknightsService;
    @Autowired
    private AgentService agentService;

    @GetMapping("getAccount")
    public JsonResult getAccount(){
        List<AccountInfo> accountInfos = arknightsService.selectAllAccount();
        return JsonResult.success(accountInfos);
    }

    @GetMapping("getPoolName")
    public String FoundOne(
            @RequestParam String name
    ){
        return name;
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
