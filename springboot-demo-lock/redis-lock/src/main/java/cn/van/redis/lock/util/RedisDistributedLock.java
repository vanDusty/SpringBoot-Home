package cn.van.redis.lock.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;
import java.util.UUID;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: JedisUtil
 *
 * @author: Van
 * Date:     2019-09-18 18:55
 * Description: Redis 分布式锁工具类
 * Version： V1.0
 */
@Component
public class RedisDistributedLock {
    /**
     * 成功获取锁标示
     */
    private static final String LOCK_SUCCESS = "OK";
    /**
     * 成功解锁标示
     */
    private static final Long RELEASE_SUCCESS = 1L;

    @Autowired
    private JedisPool jedisPool;

    /**
     * redis 数据存储过期时间
     */
    final int expireTime = 500;


    /**
     * 尝试获取分布式锁
     * @param lockKey 锁
     * @param lockValue 请求标识
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, String lockValue) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String result = jedis.set(lockKey, lockValue, "NX", "PX", expireTime);
            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }
        } finally {
            //归还 jedis 连接
            if(jedis != null){
                jedis.close();
            }
        }
        return false;
    }

    /**
     * 释放分布式锁
     * @param lockKey 锁
     * @param lockValue 请求标识
     * @return 是否释放成功
     */
    public boolean unLock(String lockKey, String lockValue) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
        } finally {
            //归还 jedis 连接
            if(jedis != null){
                jedis.close();
            }
        }
        return false;
    }
}
