/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: RedissLockUtil
 * Author:   zhangfan
 * Date:     2019-06-18 15:05
 * Description: Rediss分布式锁工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.redisLock.redisson.utils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 〈〉<br>
 * 〈Rediss分布式锁工具类〉
 * @author zhangfan
 * @create 2019-06-18
 * @since 1.0.0
 */
@Component
public class RedissLockUtil {

    private static RedissonClient redissonClient = null;

    /**
     * 加锁: 最常见的使用方法
     * @param lockKey
     * @return
     */
    public static void lock(String lockKey) {
        RLock rLock = redissonClient.getLock(lockKey);
        rLock.lock();
    }

    /**
     * 释放锁
     * @param lockKey
     */
    public void unlock(String lockKey) {
        RLock rLock = redissonClient.getLock(lockKey);
        rLock.unlock();
    }

    /**
     * 带超时的锁: 超过加锁时间后自动解锁
     * @param lockKey
     * @param unit 时间单位
     * @param leaseTime 超时时间
     */
    public void lock(String lockKey, TimeUnit unit , Long leaseTime) {
        RLock rLock = redissonClient.getLock(lockKey);
        rLock.lock(leaseTime, unit);
    }

    /**
     * 尝试获取锁
     * @param lockKey
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @param unit 时间单位
     * @return
     */
    public boolean tryLock(String lockKey, int waitTime, int leaseTime, TimeUnit unit) throws InterruptedException {
        RLock rLock = redissonClient.getLock(lockKey);
        return rLock.tryLock(waitTime, leaseTime, unit);
    }

    /**
     * 更多锁，详见：https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8
     */
}