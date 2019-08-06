/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: Scheduler2Task
 * Author:   zhangfan
 * Date:     2019-03-08 15:50
 * Description: 定时任务2
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈定时任务2:多线程执行〉
 *
 * @author zhangfan
 * @create 2019-03-08
 * @since 1.0.0
 */
@Component
@Async
public class Scheduler2Task {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerTask.class);

    @Scheduled(cron = "0/5 * * * * *")
    public void scheduled(){
        logger.info("=====>>>>>使用cron执行定时任务-1");
    }

    @Scheduled(cron="*/6 * * * * ?")
    public void scheduled3(){
        logger.info("=====>>>>>使用cron执行定时任务-2");
    }

}