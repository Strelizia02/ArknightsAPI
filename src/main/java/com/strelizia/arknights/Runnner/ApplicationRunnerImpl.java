package com.strelizia.arknights.Runnner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始登录QQ");
        Bot bot = BotFactory.INSTANCE.newBot(123456, "test");
    }
}
