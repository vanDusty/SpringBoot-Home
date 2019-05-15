/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: TestMysql
 * Author:   zhangfan
 * Date:     2019-05-15 15:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.shiro;

import cn.van.shiro.entity.SysUserDO;
import cn.van.shiro.mapper.SysUserDao;
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
 * @create 2019-05-15
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMysql {

    @Resource
    SysUserDao userDao;

    @Test
    public void test() {
        SysUserDO userDO = new SysUserDO();
        userDO.setId(1l);
        userDO.setUserName("van");
        userDO.setPassword("ddd");
        userDao.insert(userDO);
    }
}