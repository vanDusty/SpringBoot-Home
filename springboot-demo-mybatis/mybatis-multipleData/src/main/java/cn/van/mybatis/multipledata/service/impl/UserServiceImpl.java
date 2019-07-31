package cn.van.mybatis.multipledata.service.impl;

import cn.van.mybatis.multipledata.annotation.DataSource;
import cn.van.mybatis.multipledata.entity.User;
import cn.van.mybatis.multipledata.mapper.UserMapper;
import cn.van.mybatis.multipledata.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserServiceImpl
 *
 * @author: Van
 * Date:     2019-07-30 17:44
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;
    @Override
    public List<User> selectMasterAll() {
        return userMapper.selectAll();
    }

    @DataSource("slave1")
    @Override
    public List<User> selectSlave1() {
        return userMapper.selectAll();
    }

    @DataSource("slave2")
    @Override
    public List<User> selectSlave2() {
        return userMapper.selectAll();
    }
}
