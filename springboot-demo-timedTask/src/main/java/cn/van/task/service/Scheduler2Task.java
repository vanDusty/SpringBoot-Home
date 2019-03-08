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

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br> 
 * 〈定时任务2〉
 *
 * @author zhangfan
 * @create 2019-03-08
 * @since 1.0.0
 */
@Component
public class Scheduler2Task {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 6000)
    public void reportCurrentTime() {
        System.out.println("现在时间：" + dateFormat.format(new Date()));
    }
}