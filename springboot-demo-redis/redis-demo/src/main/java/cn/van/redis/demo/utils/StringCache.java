/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: StringCache
 * Author:   zhangfan
 * Date:     2019-03-25 11:32
 * Description: Redis String类型操作工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.redis.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br> 
 * 〈Redis String类型操作工具类〉
 *
 * @author zhangfan
 * @create 2019-03-25
 * @since 1.0.0
 */
@Component
public class StringCache {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 实现命令：SET key value，设置一个key-value（将字符串值 value关联到 key）
     *
     * @param key
     * @param value
     */
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param value
     * @param timeout （以秒为单位）
     */
    public void setValue(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 实现命令：DEL key，删除一个key
     *
     * @param key
     */
    public void delKey(String key) {
        redisTemplate.delete(key);
    }
    /**
     * 实现命令：DEL key，删除一个key
     *
     * @param key
     */
    public boolean existKey(String key) {
        return redisTemplate.hasKey(key);
    }
    /**
     * 实现命令：GET key，返回 key所关联的字符串值。
     *
     * @param key
     * @return value
     */
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     *
     * @param key
     * @return
     */
    public long getRemainingTime(String key) {
        return redisTemplate.getExpire(key);
    }
}