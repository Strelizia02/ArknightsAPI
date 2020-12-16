package com.strelizia.arknights.job;

import com.strelizia.arknights.dao.UserFoundMapper;
import com.strelizia.arknights.model.UserGetInfo;
import com.strelizia.arknights.util.SendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/16 14:10
 **/
@Component
@Slf4j
public class DailyCountJob {

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Autowired
    private UserFoundMapper userFoundMapper;

    //每天晚上8点发送当日统计结果
    @Scheduled(cron = "${scheduled.countJob}")
    @Async
    public void dailyCountJob(){
        List<Long> groups = userFoundMapper.selectAllActiveGroups();
        for (Long groupId:groups){
            String s = jobMessageByGroupId(groupId);
            String imgUrl = "http://gchat.qpic.cn/gchatpic_new/412459523/901158551-2534335053-0917C33A201AC78760663890D4073968/0?vuin=3022645754\\u0026term=255\\u0026pictype=0";
            sendMsgUtil.CallOPQApiSendImg(groupId,s,imgUrl);
        }
        userFoundMapper.giveMoreFoundToFeiQiu();
    }

    public String jobMessageByGroupId(Long groupId){
        UserGetInfo allSixMax = userFoundMapper.selectAllSixMax(groupId);
        UserGetInfo allRateMax = userFoundMapper.selectRateAllMax(groupId);
        UserGetInfo todaySixMax = userFoundMapper.selectTodaySixMax(groupId);
        UserGetInfo todayRateMax = userFoundMapper.selectRateTodayMax(groupId);
        UserGetInfo foundCount = userFoundMapper.selectFoundCountMax(groupId);
        UserGetInfo todayFeiQiu = userFoundMapper.selectRateTodayMin(groupId);
        Integer usersCount = userFoundMapper.foundNoSixToday(groupId);
        String s = "截止今晚8:00，本群内";
        if (allRateMax != null)s = s + "模拟寻访累计出货最多的欧皇为：" + allSixMax.getName() + "累计获得"+ allSixMax.getValue() + "个六星\n";
        if (allRateMax != null)s = s + "模拟寻访累计爆率最高的欧皇为：" + allRateMax.getName() + "六星概率为："+ allRateMax.getValue() + "个六星\n";
        if (todaySixMax != null)s = s + "今日获取六星最多的是：" + todaySixMax.getName() + "共抽取了" + todaySixMax.getValue() + "个六星\n";
        if (foundCount != null)s = s +  "目前垫刀数最多的人为：" + foundCount.getName() + "垫刀数为：" + foundCount.getValue() + "加油，也许下一发就出货了\n";
        s = s +"今日群内共有" + usersCount + "个群友已经50抽还没六星，现在白给这些群友二十抽，抓紧时间，这可能就是去往欧洲的船票\n";
        if (todayFeiQiu != null)s = s + "今日非酋为：" + todayFeiQiu.getName() + "五星六星爆率合计竟高达" + todayFeiQiu.getValue() + "人与人的欧气果然不能相提并论\n";
        if (todayRateMax != null)s = s + "今日欧皇为：" + todayRateMax.getName() + "六星爆率竟高达" + todayRateMax.getValue() + "让我们恭喜这个b";
        return s;
    }
}
