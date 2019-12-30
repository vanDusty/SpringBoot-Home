package cn.van.mybatis.pageable;

import cn.van.mybatis.pageable.entity.UserInfoDO;
import cn.van.mybatis.pageable.page.PageInterceptor;
import cn.van.mybatis.pageable.mapper.UserMapper;
import cn.van.mybatis.pageable.page.PageResult;
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
 * Description: 分页测试
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
        // 该查询进行分页，指定第几页和每页数量
        PageInterceptor.startPage(1,0);
        List<UserInfoDO> all = dao.findAll();
        PageResult<UserInfoDO> result = new PageResult<>(all);
        // 分页结果打印
        System.out.println("总记录数：" + result.getTotal());
        System.out.println(result.getData().toString());
    }
}
