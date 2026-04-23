package com.example.pcmalluserservice.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@MapperScan("com.example.pcmalluserservice.dao")
public class MybatisPlusConfig {
    public MybatisPlusConfig() {
        log.debug("创建 MybatisPlusConfig：{}", this);
    }
}
