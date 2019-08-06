/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: AsyncConfig
 * Author:   zhangfan
 * Date:     2019-03-10 16:28
 * Description: task的配置
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.task.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 〈一句话功能简述〉<br> 
 * 〈task的配置〉
 *
 * @author zhangfan
 * @create 2019-03-10
 * @since 1.0.0
 */
@Configuration // 表明该类是一个配置类
@EnableAsync // 开启异步事件的支持
public class AsyncConfig {

    @Value("${myProps.corePoolSize}")
    private int corePoolSize;
    @Value("${myProps.maxPoolSize}")
    private int maxPoolSize;
    @Value("${myProps.queueCapacity}")
    private int queueCapacity;

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.initialize();
        return executor;
    }
}