package cn.van.mybatis.druid.web.controller;


import cn.van.mybatis.druid.entity.UserDO;
import cn.van.mybatis.druid.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserController
 *
 * @author: Van
 * Date:     2019-07-26 12:07
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/selectOne")
    public UserDO selectOne(Long id) {
        return userService.selectById(id);
    }

    @GetMapping("/selectAll")
    public List<UserDO> selectAll() {
        return userService.selectAll();
    }
}
