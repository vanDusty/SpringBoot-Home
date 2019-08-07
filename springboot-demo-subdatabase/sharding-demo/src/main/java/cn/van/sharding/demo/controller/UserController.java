package cn.van.sharding.demo.controller;

import cn.van.sharding.demo.entity.User;
import cn.van.sharding.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname UserController
 * @Description
 * @Author Van
 * @Date 2019-07-26 17:39
 * @Version 1.0
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/select")
    public List<User> select() {
        return userService.getUserList();
    }

    @GetMapping("/insert")
    public int insert(User user) {
        return userService.save(user);
    }

}
