package cn.van.springboot.task.config.service;

import cn.van.springboot.task.config.entity.ScheduledJob;

/**
 * Copyright (C), 2017-2022, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ScheduledTaskService
 *
 * @Author: VanFan
 * Date:     2022/2/9 8:26 PM
 * Description:
 * Version： V1.0
 */
public interface ScheduledTaskService{

    Boolean start(ScheduledJob scheduledJob);

    Boolean stop(String jobKey);

    Boolean restart(ScheduledJob scheduledJob);

    void initTask();
}
