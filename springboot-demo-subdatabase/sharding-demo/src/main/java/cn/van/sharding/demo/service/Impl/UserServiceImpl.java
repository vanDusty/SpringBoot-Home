package cn.van.sharding.demo.service.Impl;

import cn.van.sharding.demo.entity.User;
import cn.van.sharding.demo.mapper.UserMapper;
import cn.van.sharding.demo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname UserServiceImpl
 * @Description
 * @Author Van
 * @Date 2019-07-26 17:31
 * @Version 1.0
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;
    @Override
    public int save(User entity) {
        return userMapper.insert(entity);
    }

    @Override
    public List<User> getUserList() {
        return userMapper.selectAll();
    }

}
