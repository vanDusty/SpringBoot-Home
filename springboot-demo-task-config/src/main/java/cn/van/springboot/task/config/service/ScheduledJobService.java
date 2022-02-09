package cn.van.springboot.task.config.service;

import cn.van.springboot.task.config.entity.ScheduledJob;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Copyright (C), 2017-2022, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ScheduledJobService
 *
 * @Author: VanFan
 * Date:     2022/2/9 8:29 PM
 * Description:
 * Version： V1.0
 */
public interface ScheduledJobService{

    /**
     * 修改定时任务，并重新启动
     * @param scheduledJob
     * @return
     */
    boolean updateOne(ScheduledJob scheduledJob);

}
