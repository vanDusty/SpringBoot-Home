package cn.van.springboot.task.config.bean;

import cn.van.springboot.task.config.service.ScheduledTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Copyright (C), 2017-2022, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ScheduledTaskRunner
 *
 * @Author: VanFan
 * Date:     2022/2/9 8:22 PM
 * Description: 初始化定时任务
 * Version： V1.0
 */
@Slf4j
@Component
public class ScheduledTaskRunner implements ApplicationRunner {

    @Resource
    private ScheduledTaskService scheduledTaskService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("----初始化定时任务开始----");
        scheduledTaskService.initTask();
        log.info("----初始化定时任务完成----");
    }
}


