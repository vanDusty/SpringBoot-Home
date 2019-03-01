/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: AopServiceTest
 * Author:   zhangfan
 * Date:     2019-02-27 14:57
 * Description: aop测试类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br> 
 * 〈aop测试类〉
 *
 * @author zhangfan
 * @create 2019-02-27
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AopServiceTest {

    @Resource
    AopService aopService;

    @Test
    public void insertLog() {
        aopService.insertLog(1l,"van");
    }

    @Test
    public void throwException() {
        aopService.throwException("模拟抛出异常");
    }
}