package cn.van.redis.view.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: RedisUtils
 *
 * @author: Van
 * Date:     2019-03-26 19:17
 * Description: Redis 工具类
 * Version： V1.0
 */
@Component
public  class RedisUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        redisTemplate.delete(key[0]);
    }

    /**
     * 计数
     * @param key
     * @param value
     */
    public Long add(String key, Object... value) {
        return redisTemplate.opsForHyperLogLog().add(key,value);
    }
    /**
     * 获取总数
     * @param key
     */
    public Long size(String key) {
        return redisTemplate.opsForHyperLogLog().size(key);
    }

}
