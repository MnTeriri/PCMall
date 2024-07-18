package com.example.pcmallprovidergoods.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class ThreadPoolConfig {
    public ThreadPoolConfig() {
        log.debug("创建配置类对象：{}", this);
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(200);//最大可创建的线程数
        executor.setCorePoolSize(50);//核心线程池大小
        executor.setQueueCapacity(1000);//队列最大长度
        executor.setKeepAliveSeconds(300);//线程池维护线程所允许的空闲时间
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//线程池对拒绝任务(无线程可用)的处理策略
        return executor;
    }
}
