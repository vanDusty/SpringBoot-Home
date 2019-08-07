package cn.van.sharding.demo.service;

import cn.van.sharding.demo.entity.User;

import java.util.List;

/**
 * @Classname UserService
 * @Description
 * @Author Van
 * @Date 2019-07-26 17:31
 * @Version 1.0
 */
public interface UserService{

    /**
     * 保存用户信息
     * @param entity
     * @return
     */
    int save(User entity);

    /**
     * 查询全部用户信息
     * @return
     */
    List<User> getUserList();
}
