package cn.van.mybatis.pageable;

import cn.van.mybatis.pageable.entity.UserInfoDO;
import cn.van.mybatis.pageable.interceptor.PageInterceptor;
import cn.van.mybatis.pageable.mapper.UserMapper;
import cn.van.mybatis.pageable.model.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

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

    // @Resource
    // UserService userService;

    @Resource
    private UserMapper dao;

    @Test
    public void selectForPage() {

        PageInterceptor.startPage(0,2);
        List<UserInfoDO> all = dao.findAll();
        PageResult<UserInfoDO> modelPageResult = new PageResult<>(all);

        System.out.println(modelPageResult.getData().toString());
        // PageParam pageParam = new PageParam(1, 2);
        // PageData<UserInfoDO> pageData = userService.selectForPage(pageParam);
        // System.out.println(pageData);
    }
}
