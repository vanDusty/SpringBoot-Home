package cn.van.mybatis.plus.demo.web.controller;

import cn.van.mybatis.plus.demo.entity.UserDO;
import cn.van.mybatis.plus.demo.mapper.UserMapper;
import cn.van.mybatis.plus.demo.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserController
 *
 * @author: Van
 * Date:     2019-10-08 20:44
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@RestController
public class UserController {
    @Resource
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @GetMapping("/getAll")
    public List<UserDO> getAll() {
        return userMapper.selectList(null);
    }

    @PostMapping("/insertUser")
    public Integer insertUser(@RequestBody UserDO user) {
        return userMapper.insert(user);
    }

    @PostMapping("/updateUser")
    public Integer updateUser(@RequestBody UserDO user) {
        return userMapper.updateById(user);
    }

    @DeleteMapping("/deleteUser/{id}")
    public Integer deleteUser(@PathVariable("id") String id) {
        return userMapper.deleteById(id);
    }

    @GetMapping("/getUser")
    public List<UserDO> getUser() {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();

        queryWrapper
                .isNotNull("nick_name")
                .ge("age", 18);
        return userMapper.selectList(queryWrapper);
    }

    @GetMapping("/findPage")
    public IPage<UserDO> findPage() {
        Page<UserDO> page = new Page<>(1, 5);

        return userMapper.selectPage(page, null);
    }

    // @GetMapping("/getAllByService")
    // public List<UserDO> getAllByService() {
    //     return userService.list();
    // }

    // @PostMapping("/insertUserByService")
    // public Boolean insertUserByService(@RequestBody UserDO user) {
    //     return userService.save(user);
    // }
    //
    // @PostMapping("/updateUserByService")
    // public Boolean updateUserByService(@RequestBody UserDO user) {
    //     QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
    //
    //     queryWrapper.eq("id", user.getId());
    //
    //     return userService.update(user, queryWrapper);
    // }
    //
    // @DeleteMapping("/deleteUserByService/{id}")
    // public Boolean deleteUserByService(@PathVariable("id") String id) {
    //     return userService.removeById(id);
    // }
}