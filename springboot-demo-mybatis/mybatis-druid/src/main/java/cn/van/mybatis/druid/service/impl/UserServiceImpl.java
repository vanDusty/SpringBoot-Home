package cn.van.mybatis.druid.service.impl;


import cn.van.mybatis.druid.entity.UserDO;
import cn.van.mybatis.druid.mapper.UserMapper;
import cn.van.mybatis.druid.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserServiceImpl
 *
 * @author: Van
 * Date:     2019-07-26 15:07
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    public UserDO selectById(Long id) {
        return userMapper.selectById(id);
    }

    public List<UserDO> selectAll() {
        return userMapper.selectAll();
    }
}
