package cn.van.exception.global.web.controller;

import cn.van.exception.global.entity.User;
import cn.van.exception.global.exception.BizException;
import cn.van.exception.global.result.HttpResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserController
 *
 * @author: Van
 * Date:     2019-08-01 17:13
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 无异常的请求
     * @return
     */
    @GetMapping("/list")
    public HttpResult list() {
        System.out.println("开始查询...");
        List<User> userList =new ArrayList();
        User user=new User();
        user.setId(1L);
        user.setName("Van");
        user.setAge(18);
        userList.add(user);
        return HttpResult.success(userList);
    }

    /**
     * 模拟自定义异常
     * @param user
     * @return
     */
    @PostMapping("/insert")
    public HttpResult insert(@RequestBody User user) {
        System.out.println("开始新增...");
        //如果姓名为空就手动抛出一个自定义的异常！
        if(user.getName()==null){
            throw  new BizException(-1,"用户姓名不能为空！");
        }
        return new HttpResult();
    }

    /**
     * 这里故意发送post请求，模拟请求方式错误
     * @param user
     * @return
     */
    @PutMapping("/update")
    public HttpResult update(@RequestBody User user) {
        System.out.println("开始更新...");
        return new HttpResult();
    }

    /**
     * 模拟其他异常
     * @param id
     * @return
     */
    @DeleteMapping("/deleteById")
    public HttpResult deleteById(Long id)  {
        System.out.println("开始删除...");
        //这里故意造成一个异常，并且不进行处理
        Integer.parseInt("abc123");
        return new HttpResult();
    }
}
