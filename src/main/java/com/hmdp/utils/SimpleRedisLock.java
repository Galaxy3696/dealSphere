package com.hmdp.utils;


import cn.hutool.core.lang.UUID;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class SimpleRedisLock implements ILock {

    private final String name;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String ID_PREFIX = UUID.randomUUID().toString(true)+"-";
    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @Override
    public boolean tryLock(long timeoutSec) {
        String id = ID_PREFIX + Thread.currentThread().getId();
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent("lock:" + name, id + "", timeoutSec, TimeUnit.SECONDS));
    }

    @Override
    public void unlock() {
        stringRedisTemplate.delete("lock:"+name);
    }
}
