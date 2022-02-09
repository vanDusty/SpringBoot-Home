package cn.van.springboot.task.config.service.impl;

import cn.van.springboot.task.config.entity.ScheduledJob;
import cn.van.springboot.task.config.job.ScheduledTaskBase;
import cn.van.springboot.task.config.service.ScheduledJobService;
import cn.van.springboot.task.config.service.ScheduledTaskService;
import cn.van.springboot.task.config.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Copyright (C), 2017-2022, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ScheduledTaskServiceImpl
 *
 * @Author: VanFan
 * Date:     2022/2/9 8:26 PM
 * Description: 定时任务管理实现类
 * Version： V1.0
 */
@Slf4j
@Service
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

    /**
     * 定时任务线程池
     */
    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Resource
    private ScheduledJobService scheduledJobService;

    /**
     * 可重入锁
     */
    private ReentrantLock lock = new ReentrantLock();

    /**
     * 启动状态的定时任务集合
     */
    public Map<String, ScheduledFuture> scheduledFutureMap = new ConcurrentHashMap<>();

    @Override
    public void initTask() {
        List<ScheduledJob> jobList = scheduledJobService.selectAll();
        for (ScheduledJob job : jobList) {
            if (job.getStatus() == 1){
                doStartTask(job);
            }
        }
    }
    @Override
    public Boolean start(ScheduledJob scheduledJob) {
        String jobKey = scheduledJob.getJobKey();
        log.info("启动定时任务【{}】", jobKey);
        //添加锁放一个线程启动，防止多人启动多次
        lock.lock();
        try {
            if(this.isStart(jobKey)){
                log.info("当前任务在启动状态中【{}】", jobKey);
                return false;
            }
            //任务启动
            this.doStartTask(scheduledJob);
        }catch (Exception e){
            log.error("error:{}", e);
        }finally {
            lock.unlock();
            log.info("解锁完毕");
        }

        return true;
    }

    /**
     * 任务是否已经启动
     */
    private Boolean isStart(String taskKey) {
        //校验是否已经启动
        if (scheduledFutureMap.containsKey(taskKey)) {
            if (!scheduledFutureMap.get(taskKey).isCancelled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean stop(String jobKey) {
        log.info("停止任务：【{}】", jobKey);
        boolean flag = scheduledFutureMap.containsKey(jobKey);
        log.info("当前实例是否还在运行：【{}】", flag);
        if(flag){
            ScheduledFuture scheduledFuture = scheduledFutureMap.get(jobKey);
            scheduledFuture.cancel(true);
            scheduledFutureMap.remove(jobKey);
        }
        return flag;
    }

    @Override
    public Boolean restart(ScheduledJob scheduledJob) {
        log.info("重启定时任务"+scheduledJob.getJobKey());
        //停止后再启动
        this.stop(scheduledJob.getJobKey());
        return this.start(scheduledJob);
    }

    /**
     * 执行启动任务
     */
    public void doStartTask(ScheduledJob job){
        log.info("开始启动定时任务：【{}】", job.getJobKey());
        Class<?> clazz;
        ScheduledTaskBase task;
        try {
            clazz = Class.forName(job.getJobKey());
            task = (ScheduledTaskBase) SpringContextUtil.getBean(clazz);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("spring_scheduled_cron表数据" + job.getJobKey() + "有误", e);
        }
        Assert.isAssignable(ScheduledTaskBase.class, task.getClass(), "定时任务类必须实现ScheduledOfTask接口");
        // 将当前任务放入定时任务线程池中
        ScheduledFuture scheduledFuture = threadPoolTaskScheduler.schedule(task,(triggerContext -> new CronTrigger(job.getCronExpression()).nextExecutionTime(triggerContext)));
        // 该任务放进已启动的定时任务集合中
        scheduledFutureMap.put(job.getJobKey(),scheduledFuture);
    }

}
