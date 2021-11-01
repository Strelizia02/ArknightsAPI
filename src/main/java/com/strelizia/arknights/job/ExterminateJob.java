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
            sendMsgUtil.CallOPQApiSendMsg(groupId, "我是本群剿灭小助手，今天是本周最后一天，博士不要忘记打剿灭哦❤\n道路千万条，剿灭第一条\n剿灭忘记打，博士两行泪\n洁哥主页：http://www.angelina-bot.top/", 2);
//            sendMsgUtil.CallOPQApiSendXml(groupId, "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID='1' templateID='1' action='' brief='&#91;剿灭小助手&#93;' sourceMsgId='0' url='http://www.angelina-bot.top/' flag='2' adverSign='0' multiMsgFlag='0'><item layout='0'><title size='38' color='#9900CC' style='1'>博士你打剿灭了吗</title></item><item layout='0'><hr hidden='false' style='0' /></item><item layout='6'><summary color='#FF0033'>今天是本周最后一天，博士不要忘记打剿灭哦❤</summary><summary color='#FF0099'>\uD83D\uDCAA道路千万条，剿灭第一条\uD83D\uDCAA&#x000D;\uD83D\uDE2D剿灭忘记打，博士两行泪\uD83D\uDE2D</summary></item><source name='' icon='' action='' appid='-1' /></msg>", 2);
            try {
                Thread.sleep(3000);
            }catch (InterruptedException ignored){

            }
        }
    }
}
