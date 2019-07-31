package cn.van.mybatis.multipledata;

import cn.van.mybatis.multipledata.entity.User;
import cn.van.mybatis.multipledata.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserMapperTest
 *
 * @author: Van
 * Date:     2019-07-30 15:58
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserMapperTest {

    @Resource
    private UserService userService;

    /**
     * 测试主库
     */
    @Test
    public void selectMaster() {
       List<User> users =  userService.selectMasterAll();
            users.forEach(user -> {
                System.out.println("id:" + user.getId());
                System.out.println("name:" + user.getUserName());
                System.out.println("password:" + user.getUserAge());
            });
    }

    /**
     * 测试从库slave1
     */
    @Test
    public void selectSlave1() {
        List<User> users =  userService.selectSlave1();
        users.forEach(user -> {
            System.out.println("id:" + user.getId());
            System.out.println("name:" + user.getUserName());
            System.out.println("password:" + user.getUserAge());
        });
    }

    /**
     * 测试从库slave2
     */
    @Test
    public void selectSlave2() {
        List<User> users =  userService.selectSlave2();
        users.forEach(user -> {
            System.out.println("id:" + user.getId());
            System.out.println("name:" + user.getUserName());
            System.out.println("password:" + user.getUserAge());
        });
    }

}
