package cn.van.springboot.task.config.service.impl;

import cn.van.springboot.task.config.entity.ScheduledJob;
import cn.van.springboot.task.config.mapper.ScheduledJobMapper;
import cn.van.springboot.task.config.service.ScheduledJobService;
import cn.van.springboot.task.config.service.ScheduledTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (C), 2017-2022, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ScheduledJobServiceImpl
 *
 * @Author: VanFan
 * Date:     2022/2/9 8:30 PM
 * Description:
 * Version： V1.0
 */
@Service
@Slf4j
public class ScheduledJobServiceImpl implements ScheduledJobService {

    @Resource
    private ScheduledTaskService scheduledTaskService;

    @Resource
    private ScheduledJobMapper scheduledJobMapper;

    @Override
    public boolean updateOne(ScheduledJob scheduledJob) {
        if(1 == scheduledJobMapper.updateById(scheduledJob)){
            return scheduledTaskService.restart(scheduledJobMapper.selectById(scheduledJob.getJobId()));
        }
        return true;
    }

    @Override
    public List<ScheduledJob> selectAll() {
        return scheduledJobMapper.selectList(null);
    }
}
