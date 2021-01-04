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

    /**
     * 手动更新方法，kokodayo的端口使用springhttp拨不通很烦
     * 被阿里云OSS防盗链锁住了，但是postman可以拨通就离谱
     * 目前处理方法为使用postman获取结果json，然后发送，只能更新干员信息
     */
    @PostMapping("update")
    public Integer receive(
            @RequestBody String json
    ){
        Integer i = updateDataService.updateByJson(json);
        return i;
    }

    @GetMapping("AutoUpdate")
    public Integer update(){
        Integer s = updateDataService.updateAllData();
        return s;
    }

    //获取所有涩图到本地
    @PostMapping("getImg")
    public Integer getAllImg(
            @RequestBody String dir
    ){
        Integer i = seTuService.getAllImageIntoLocal(dir);
        return i;
    }
}
