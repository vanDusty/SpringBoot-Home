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

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br> 
 * 〈定时任务1〉
 *
 * @author zhangfan
 * @create 2019-03-08
 * @since 1.0.0
 */
@Component
public class SchedulerTask {
    private int count=0;

    @Scheduled(cron="*/6 * * * * ?")
    private void process(){
        System.out.println("this is scheduler task runing  "+(count++));
    }
}