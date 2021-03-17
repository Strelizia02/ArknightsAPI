package com.strelizia.arknights.job;

import com.strelizia.arknights.service.UpdateDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wangzy
 * @Date 2020/12/16 14:10
 **/
@Component
@Slf4j
public class UpdateJob {

    @Autowired
    private UpdateDataService updateDataService;

    //每5分钟判断是否有数据更新
    @Scheduled(cron = "${scheduled.updateJob}")
    @Async
    public void uapdateJob(){
        updateDataService.updateAllData();
    }
}
