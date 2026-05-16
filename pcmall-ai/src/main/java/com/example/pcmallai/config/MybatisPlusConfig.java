package com.example.pcmallai.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@MapperScan("com.example.pcmallai.dao")
public class MybatisPlusConfig {
    public MybatisPlusConfig() {
        log.debug("创建 MybatisPlusConfig：{}", this);
    }
}