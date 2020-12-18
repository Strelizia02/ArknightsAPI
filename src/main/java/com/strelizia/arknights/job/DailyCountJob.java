package com.strelizia.arknights.job;

import com.strelizia.arknights.dao.UserFoundMapper;
import com.strelizia.arknights.model.UserGetInfo;
import com.strelizia.arknights.service.DailyCountService;
import com.strelizia.arknights.util.SendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/16 14:10
 **/
@Component
@Slf4j
public class DailyCountJob {

    @Autowired
    private DailyCountService dailyCountService;

    //每天晚上8点发送当日统计结果
    @Scheduled(cron = "${scheduled.countJob}")
    @Async
    public void dailyCountJob(){
        dailyCountService.SendDailyCount();
    }

}
