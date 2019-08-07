package cn.van.sharding.demo.mapper;

import cn.van.sharding.demo.entity.User;

import java.util.List;

/**
 * @Classname UserMapper
 * @Description 用户Mapper 接口
 * @Author Van
 * @Date 2019-07-26 17:25
 * @Version 1.0
 */
public interface UserMapper{

    int insert(User user);

    List<User> selectAll();

}
