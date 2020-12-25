package com.strelizia.arknights.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wangzy
 * @Date 2020/12/25 14:38
 **/
@Configuration
public class ThreadPoolTaskConfig {
    @Bean("taskModuleExecutor")
    public Executor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setKeepAliveSeconds(3);
        executor.setQueueCapacity(20);
        executor.setThreadNamePrefix("arknights");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

}
