package cn.van.redis.demo; /**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: cn.van.redis.demo.StringCacheTest
 * Author:   zhangfan
 * Date:     2019-03-22 11:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @公众号： 风尘博客
 * @Classname StringCacheTest
 * @Description Redis 测试
 * @Date 2019/3/19 11:32 下午
 * @Author by Van
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class StringCacheTest {

    private static final Logger logger = LoggerFactory.getLogger(StringCacheTest.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 测试 StringRedisTemplate
     */
    @Test
    public void stringRedisTemplateTest() {
        stringRedisTemplate.opsForValue().set("name","redis测试");
        String name = stringRedisTemplate.opsForValue().get("name");
        logger.info(name);
        stringRedisTemplate.delete("name");
        name = stringRedisTemplate.opsForValue().get("name");
        logger.info(name);
    }
    /**
     * 测试 RedisTemplate
     */
    @Test
    public void redisTemplateTest() {
        redisTemplate.opsForValue().set("name","redis测试");
        String name = (String) redisTemplate.opsForValue().get("name");
        logger.info(name);
        redisTemplate.delete("name");
        name = (String) redisTemplate.opsForValue().get("name");
        logger.info(name);
    }

}