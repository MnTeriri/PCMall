package com.example.pcmallproviderstorage.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {
    public CacheConfig() {
        log.debug("创建配置类对象：{}", this);
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        //自定义Spring Cache的RedisCacheConfiguration
        //设置为Jackson序列化
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))//设置缓存有效期为5分钟
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
