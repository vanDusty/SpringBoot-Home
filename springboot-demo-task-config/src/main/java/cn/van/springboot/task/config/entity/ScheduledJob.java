package cn.van.springboot.task.config.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Copyright (C), 2017-2022, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ScheduledJob
 *
 * @Author: VanFan
 * Date:     2022/2/9 8:29 PM
 * Description:
 * Version： V1.0
 */
@Data
@TableName("scheduled_job")
public class ScheduledJob {

    @TableId(value = "job_id",type = IdType.AUTO)
    private Integer jobId;

    private String jobKey;

    private String cronExpression;

    private String taskExplain;

    private Integer status;

}
