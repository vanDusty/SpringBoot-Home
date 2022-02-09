package cn.van.springboot.task.config.job.impl;

import cn.van.springboot.task.config.job.ScheduledTaskBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Copyright (C), 2017-2022, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ScheduledTaskOne
 *
 * @Author: VanFan
 * Date:     2022/2/9 8:24 PM
 * Description: 定时任务2
 * Version： V1.0
 */
@Slf4j
@Component
public class ScheduledTaskTwo implements ScheduledTaskBase {
    @Override
    public void execute() {
        log.info("执行任务2 "+ LocalDateTime.now());
    }
}
