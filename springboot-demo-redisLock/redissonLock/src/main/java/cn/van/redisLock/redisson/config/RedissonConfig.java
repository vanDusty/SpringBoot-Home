/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: RedissonConfig
 * Author:   zhangfan
 * Date:     2019-06-18 14:42
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.redisLock.redisson.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 〈redisson客户端 配置〉<br>
 * 〈https://github.com/redisson/redisson/wiki/2.-%E9%85%8D%E7%BD%AE%E6%96%B9%E6%B3%95〉
 *
 * @author zhangfan
 * @create 2019-06-18
 * @since 1.0.0
 */
@Configuration
public class RedissonConfig {

    /**
     * redisson客户端：单机
     * @return
     */
    @Bean(destroyMethod="shutdown")
    public RedissonClient redisson() {
        // 构造redisson实现分布式锁必要的Config
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec())
                .useSingleServer()
                .setAddress("redis://47.98.178.84:6379").setPassword("van12345");
        return Redisson.create(config);
    }
}