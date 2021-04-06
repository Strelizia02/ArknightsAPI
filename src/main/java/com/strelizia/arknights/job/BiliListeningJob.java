package com.strelizia.arknights.job;

import com.strelizia.arknights.service.BiliListeningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author wangzy
 * @Date 2020/12/16 14:10
 **/
@Component
@Slf4j
public class BiliListeningJob {

    @Autowired
    private BiliListeningService biliListeningService;

    //每天晚上8点发送当日统计结果
    @Scheduled(cron = "${scheduled.biliJob}")
    @Async
    public void dailyCountJob(){
        biliListeningService.getDynamicList();
        log.info("拉取最新动态中{}",new Date());
    }
}
