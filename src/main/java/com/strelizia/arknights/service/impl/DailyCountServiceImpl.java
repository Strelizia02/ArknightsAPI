package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.UserFoundMapper;
import com.strelizia.arknights.model.UserGetInfo;
import com.strelizia.arknights.service.DailyCountService;
import com.strelizia.arknights.util.SendMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangzy
 * @Date 2020/12/18 11:17
 **/
@Service
public class DailyCountServiceImpl implements DailyCountService {

    @Autowired
    private UserFoundMapper userFoundMapper;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Autowired
    private GroupAdminInfoServiceImpl groupAdminInfoService;

    /**
     * 每日日报的查询逻辑
     * @param groupId
     * @return
     */
    public String jobMessageByGroupId(Long groupId){
        Integer todayCount = groupAdminInfoService.getGroupFoundAdmin(groupId);

        UserGetInfo allSixMax = userFoundMapper.selectAllSixMax(groupId);
        UserGetInfo allRateMax = userFoundMapper.selectRateAllMax(groupId);
        UserGetInfo todaySixMax = userFoundMapper.selectTodaySixMax(groupId);
        UserGetInfo todayRateMax = userFoundMapper.selectRateTodayMax(groupId);
        UserGetInfo foundCount = userFoundMapper.selectFoundCountMax(groupId);
        UserGetInfo todayFeiQiu = userFoundMapper.selectRateTodayMin(groupId);
        Integer usersCount = userFoundMapper.foundNoSixToday(groupId, todayCount);
        StringBuilder s = new StringBuilder("截止今晚8:00，本群内:\n");
        if (allRateMax != null)
            s.append("模拟寻访累计出货最多的欧皇为：").append(allSixMax.getName()).append("累计获得").append(allSixMax.getValue()).append("个六星\n\n");
        if (allRateMax != null)
            s.append("模拟寻访累计爆率最高的欧皇为：").append(allRateMax.getName()).append("六星概率为：").append(allRateMax.getValue()).append("%\n\n");
        if (todaySixMax != null)
            s.append("今日获取六星最多的是：").append(todaySixMax.getName()).append("共抽取了").append(todaySixMax.getValue()).append("个六星\n\n");
        if (foundCount != null)
            s.append("目前垫刀数最多的人为：").append(foundCount.getName()).append("垫刀数为：").append(foundCount.getValue()).append("加油，也许下一发就出货了\n\n");
        s.append("今日群内共有").append(usersCount).append("个群友机会用完还没六星，现在白给这些群友十抽，抓紧时间，这可能就是去往欧洲的船票\n\n");
        if (todayFeiQiu != null)
            s.append("今日非酋为：").append(todayFeiQiu.getName()).append("五星六星爆率合计竟高达").append(todayFeiQiu.getValue()).append("%人与人的欧气果然不能相提并论\n\n");
        if (todayRateMax != null)
            s.append("今日欧皇为：").append(todayRateMax.getName()).append("六星爆率为").append(todayRateMax.getValue()).append("%让我们恭喜这个b");
        return s.toString();
    }

    @Override
    public void SendDailyCount() {
        List<Long> groups = userFoundMapper.selectAllActiveGroups();
        for (Long groupId:groups){
            Integer todayCount = groupAdminInfoService.getGroupFoundAdmin(groupId);
            String s = jobMessageByGroupId(groupId);
            sendMsgUtil.CallOPQApiSendMsg(groupId,s,2);
            userFoundMapper.giveMoreFoundToFeiQiu(groupId, todayCount);
        }
    }
}
