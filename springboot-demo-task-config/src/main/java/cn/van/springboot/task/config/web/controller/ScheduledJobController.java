package cn.van.springboot.task.config.web.controller;

import cn.van.springboot.task.config.entity.ScheduledJob;
import cn.van.springboot.task.config.service.ScheduledJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright (C), 2017-2022, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ScheduledJobController
 *
 * @Author: VanFan
 * Date:     2022/2/9 8:36 PM
 * Description:
 * Version： V1.0
 */
@RestController
@RequestMapping("/job")
public class ScheduledJobController {

    @Autowired
    private ScheduledJobService scheduledJobService;

    @PostMapping(value = "/update")
    public String update(HttpServletRequest request, ScheduledJob scheduledJob){
        if(scheduledJobService.updateOne(scheduledJob)){
            return "修改成功";
        }
        return "修改失败";
    }

}
