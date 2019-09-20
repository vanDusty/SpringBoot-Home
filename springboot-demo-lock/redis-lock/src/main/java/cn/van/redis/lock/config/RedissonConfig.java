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
package cn.van.redis.lock.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: RedisConfig
 *
 * @author: Van
 * Date:     2019-09-19 23:47
 * Description: Redisson 配置，详见
 * https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95
 * Version： V1.0
 */

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;

    /**
     * RedissonClient,单机模式
     * @return
     * @throws IOException
     */
    @Bean
    public RedissonClient redissonSentinel() {
        //支持单机，主从，哨兵，集群等模式,此为单机模式
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password);
        return Redisson.create(config);
    }
}