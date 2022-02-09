package cn.van.springboot.task.config.service;

import cn.van.springboot.task.config.entity.ScheduledJob;

/**
 * Copyright (C), 2017-2022, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ScheduledTaskService
 *
 * @Author: VanFan
 * Date:     2022/2/9 8:26 PM
 * Description: 定时任务管理接口
 * Version： V1.0
 */
public interface ScheduledTaskService{

    /**
     * 初始化定时任务
     */
    void initTask();

    /**
     * 启动定时任务
     * @param scheduledJob
     * @return
     */
    Boolean start(ScheduledJob scheduledJob);

    /**
     * 停止任务
     * @param jobKey
     * @return
     */
    Boolean stop(String jobKey);

    /**
     * 重启定时任务
     * @param scheduledJob
     * @return
     */
    Boolean restart(ScheduledJob scheduledJob);

}
