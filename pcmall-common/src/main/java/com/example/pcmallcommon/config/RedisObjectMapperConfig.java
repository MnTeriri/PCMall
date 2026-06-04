package com.example.pcmallcommon.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Slf4j
@Configuration
public class RedisObjectMapperConfig {
    public RedisObjectMapperConfig() {
        log.debug("创建 ObjectMapperConfig：{}", this);
    }

    @Bean(name = "redisObjectMapper")
    public ObjectMapper redisObjectMapper() {
        //配置Jackson序列化时添加@class信息
        BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.example")
                .allowIfSubType("java.")
                .build();
        return JsonMapper.builder()
                .activateDefaultTypingAsProperty(typeValidator, DefaultTyping.NON_FINAL, "@class")
                .build();
    }
}
