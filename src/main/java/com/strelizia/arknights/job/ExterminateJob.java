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
                sendMsgUtil.CallOPQApiSendMsg(groupId, "我是本群剿灭小助手，今天是本周最后一天，博士不要忘记打剿灭哦❤\n道路千万条，剿灭第一条\n剿灭忘记打，博士两行泪\n洁哥主页：https://www.angelina-bot.top/", 2);
            try {
                Thread.sleep(2000);
            }catch (InterruptedException ignored){

            }
        }
    }
}
