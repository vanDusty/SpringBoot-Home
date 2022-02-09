package cn.van.springboot.task.config.job;

/**
 * Copyright (C), 2017-2022, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ScheduledTaskBase
 *
 * @Author: VanFan
 * Date:     2022/2/9 8:24 PM
 * Description: 定时任务公共接口
 * Version： V1.0
 */
public interface ScheduledTaskBase extends Runnable{

    /**
     * 任务
     */
    void execute();

    /**
     * 多线程默认方法
     */
    @Override
    default void run() {
        execute();
    }
}
