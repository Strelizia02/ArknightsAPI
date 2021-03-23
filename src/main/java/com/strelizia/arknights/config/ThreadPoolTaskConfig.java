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
    /**
     * 线程池配置信息，线程池主要用于发送消息。大图发送容易造成消息阻塞
     * @return
     */
    @Bean("taskModuleExecutor")
    public Executor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(20);
        executor.setKeepAliveSeconds(30);
        executor.setQueueCapacity(30);
        executor.setThreadNamePrefix("arknights");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

}
