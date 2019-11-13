package cn.van.mybatis.demo;

import cn.van.mybatis.demo.entity.UserDO;
import cn.van.mybatis.demo.mapper.UserMapper;
import cn.van.mybatis.demo.model.PageData;
import cn.van.mybatis.demo.model.PageParam;
import cn.van.mybatis.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: MybatisTest
 *
 * @author: Van
 * Date:     2019-07-24 16:15
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MybatisTest {

    @Resource
    UserService userService;

    @Test
    public void selectById() {
        UserDO userDO = userService.selectById(1l);
        System.out.println(userDO);
    }
    @Test
    public void selectForPage() {
        PageParam pageParam = new PageParam(1, 2);
        PageData<UserDO> pageData = userService.selectForPage(pageParam);
        System.out.println(pageData);
    }

    /**
     * 查询两次的时间对比
     */
    @Test
    public void compare() {
        PageParam pageParam = new PageParam(1,2);
        Long start = System.currentTimeMillis();
        PageData<UserDO> pageData = userService.selectForPage(pageParam);
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        PageData<UserDO> page = userService.selectForPage(pageParam);
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(page);
    }
}
