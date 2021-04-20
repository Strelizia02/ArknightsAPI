package com.strelizia.arknights.controller;

import com.strelizia.arknights.service.SeTuService;
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

    @Autowired
    private SeTuService seTuService;

    @GetMapping("update")
    public Integer update(){
        updateDataService.updateAllData(false);
        updateDataService.updateSkin();
        return 0;
    }

    @GetMapping("updateSkin")
    public Integer updateSkin(){
        updateDataService.updateSkin();
        return 0;
    }

    @GetMapping("updateItem")
    public Integer updateItem(){
        updateDataService.updateItemAndFormula();
        return 0;
    }


    //获取所有涩图到本地
    @PostMapping("getImg")
    public Integer getAllImg(
            @RequestBody String dir
    ){
        return seTuService.getAllImageIntoLocal(dir);
    }
}
