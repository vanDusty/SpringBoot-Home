/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: SchedulerTask
 * Author:   zhangfan
 * Date:     2019-03-08 15:50
 * Description: 定时任务1
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br> 
 * 〈定时任务1:单线程执行〉
 *
 * @author zhangfan
 * @create 2019-03-08
 * @since 1.0.0
 */
//@Component
public class SchedulerTask {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerTask.class);

    /**
     * @Scheduled(fixedRate = 6000) ：上一次开始执行时间点之后6秒再执行
     * @Scheduled(fixedDelay = 6000) ：上一次执行完毕时间点之后6秒再执行
     * @Scheduled(initialDelay=1000, fixedRate=6000) ：第一次延迟1秒后执行，之后按fixedRate的规则每6秒执行一次
     * @Scheduled(cron=""):详见cron表达式http://www.pppet.net/
     */

    @Scheduled(fixedRate = 5000)
    public void scheduled1() {
        logger.info("=====>>>>>使用fixedRate执行定时任务");
    }
    @Scheduled(fixedDelay = 10000)
    public void scheduled2() {
        logger.info("=====>>>>>使用fixedDelay执行定时任务");
    }

    @Scheduled(cron="*/6 * * * * ?")
    public void scheduled3(){
        logger.info("=====>>>>>使用cron执行定时任务");
    }
}