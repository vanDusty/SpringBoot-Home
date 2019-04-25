/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: TestServiceImpl
 * Author:   zhangfan
 * Date:     2019-04-25 10:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.mybatis.service.impl;


import cn.van.mybatis.dao.test1.User1Mapper;
import cn.van.mybatis.dao.User2Mapper;
import cn.van.mybatis.entity.User;
import cn.van.mybatis.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-25
 * @since 1.0.0
 */
@Service
public class TestServiceImpl implements TestService {
    @Resource
    User1Mapper user1Mapper;
    @Resource
    User2Mapper user2Mapper;

    public void insert() {
        User user = new User();
        user.setUserName("van");
        user.setPassWord("333");
        user.setUserSex("eeee");
        user1Mapper.insert(user);
    }

    public void insert2() {
        System.out.println(111);
        User user = new User();
        user.setUserName("van");
        user.setPassWord("222");
        user.setUserSex("eeee");
        user2Mapper.insert(user);
    }
}