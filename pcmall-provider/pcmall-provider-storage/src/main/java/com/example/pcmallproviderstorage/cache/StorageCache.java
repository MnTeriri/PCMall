package com.example.pcmallproviderstorage.cache;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class StorageCache implements Cache {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final String id; // RedisCache实例Id
    private final RedisTemplate<String, Object> redisTemplate;
    private static final long EXPIRE_TIME_IN_MINUTES = 30; // redis过期时间

    public StorageCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        redisTemplate = SpringUtil.getBean("redisTemplate");
        this.id = id;
        log.debug("StorageCache对象创建");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        log.debug("put object key:{} value:{}", key.toString(), value);
        redisTemplate.opsForValue().set(key.toString(), value);
        redisTemplate.expire(key.toString(), EXPIRE_TIME_IN_MINUTES, TimeUnit.MINUTES);//设置过期时间
    }

    @Override
    public Object getObject(Object key) {
        log.debug("get object key:{}", key.toString());
        return redisTemplate.opsForValue().get(key.toString());
    }

    @Override
    public Object removeObject(Object key) {
        log.debug("remove object key:{}", key);
        Object deleteObj = redisTemplate.opsForValue().get(key.toString());
        redisTemplate.delete(key.toString());
        return deleteObj;
    }

    public void clear() {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushDb();
            return null;
        });
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
}
