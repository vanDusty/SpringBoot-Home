package cn.van.mybatis.multipleData;

import cn.van.mybatis.multipleData.entity.User;
import cn.van.mybatis.multipleData.mapper.UserMapper;
import org.junit.Assert;
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
    private UserMapper userMapper;

    // @Autowired
    // private UserService userService;

    @Test
    public void save() {
        User user = new User();
        user.setUsername("master");
        user.setPassword("master");
        user.setSex(1);
        user.setAge(18);
        Assert.assertEquals(1,userMapper.save(user));
    }

    @Test
    public void update() {
        User user = new User();
        user.setId(8L);
        user.setPassword("newpassword");
        // 返回插入的记录数 ，期望是1条 如果实际不是一条则抛出异常
        Assert.assertEquals(1,userMapper.update(user));
    }

    @Test
    public void selectById() {
        User user = userMapper.selectById(2L);
        System.out.println("id:" + user.getId());
        System.out.println("name:" + user.getUsername());
        System.out.println("password:" + user.getPassword());
    }

    @Test
    public void deleteById() {
        Assert.assertEquals(1,userMapper.deleteById(1L));
    }

    @Test
    public void selectAll() {
        List<User> users= userMapper.selectAll();
        users.forEach(user -> {
            System.out.println("id:" + user.getId());
            System.out.println("name:" + user.getUsername());
            System.out.println("password:" + user.getPassword());
        });
    }

    // @Test
    // public void testTransactional() {
    //     userService.testTransactional();
    // }


}
