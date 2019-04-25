/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: MybatisTest
 * Author:   zhangfan
 * Date:     2019-03-29 12:00
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.mybatis;
import cn.van.mybatis.dao.test1.User1Mapper;
import cn.van.mybatis.dao.User2Mapper;
import cn.van.mybatis.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-03-29
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MybatisTest {


    @Resource
    User1Mapper user1Mapper;
    @Resource
    User2Mapper user2Mapper;
    @Test
    public void insert() {
        System.out.println(111);
        User user = new User();
        user.setUserName("van");
        user.setPassWord("222");
        user.setUserSex("eeee");
        user1Mapper.insert(user);
    }
    @Test
    public void insert2() {
        System.out.println(111);
        User user = new User();
        user.setUserName("Van");
        user.setPassWord("222");
        user.setUserSex("eeee");
        user2Mapper.insert(user);
    }
}