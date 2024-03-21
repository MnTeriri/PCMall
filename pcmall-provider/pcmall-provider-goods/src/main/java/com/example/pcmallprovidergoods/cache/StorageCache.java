package com.example.pcmallprovidergoods.cache;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StorageCache implements Cache {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final String id; // RedisCache实例Id
    private final StringRedisTemplate stringRedisTemplate;
    private static final long EXPIRE_TIME_IN_MINUTES = 5; // redis过期时间

    public StorageCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        stringRedisTemplate = SpringUtil.getBean("stringRedisTemplate");
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object o, Object o1) {

    }

    @Override
    public Object getObject(Object o) {
        return null;
    }

    @Override
    public Object removeObject(Object o) {
        return null;
    }

    public void clear() {
        stringRedisTemplate.execute((RedisCallback<Object>) connection -> {
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
