package com.strelizia.arknights.job;

import com.strelizia.arknights.dao.UserFoundMapper;
import com.strelizia.arknights.util.SendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ExterminateJob {
    @Autowired
    private UserFoundMapper userFoundMapper;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Scheduled(cron = "${scheduled.exterminateJob}")
    @Async
    public void exterminateJob() {
        List<Long> groups = userFoundMapper.selectAllGroups();

        for (Long groupId : groups) {
            sendMsgUtil.CallOPQApiSendMsg(groupId, "道 路 千 万 条，剿 灭 第 一 条。\n剿 灭 忘 记 打，博 士 两 行 泪。\n今天的本周最后一天，博士不要忘记打剿灭哦❤", 2);
        }
    }
}
