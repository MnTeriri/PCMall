package com.example.pcmallconsumermobile.utils;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisUtils {
    private static StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisUtils(StringRedisTemplate stringRedisTemplate) {
        log.debug("StringRedisTemplate已装配");
        RedisUtils.stringRedisTemplate = stringRedisTemplate;
    }

    public static void setCacheObject(String key, Object value) {
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(value));
        stringRedisTemplate.expire(key, 1, TimeUnit.DAYS);//设置过期时间1天
    }


    public static <T> T getCacheObject(String key, Class<T> clazz) {
        String json = stringRedisTemplate.opsForValue().get(key);
        return JSON.parseObject(json, clazz);
    }

    public static boolean deleteObject(String key) {
        return stringRedisTemplate.delete(key);
    }

}
