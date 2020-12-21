package com.strelizia.arknights.controller;

import com.strelizia.arknights.service.UpdateDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangzy
 * @Date 2020/11/20 17:02
 **/
@RequestMapping("Update")
@RestController
@Slf4j
public class UpdateController {

    @Autowired
    private UpdateDataService updateDataService;
    //返回单抽结果
    @PostMapping("update")
    public Integer receive(
            @RequestBody String json
    ){
        Integer i = updateDataService.updateByJson(json);
        return i;
    }
}
