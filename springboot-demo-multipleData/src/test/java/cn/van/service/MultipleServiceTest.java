/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: MultipleServiceTest
 * Author:   zhangfan
 * Date:     2019-02-27 14:57
 * Description: aop测试类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.service;

import cn.van.entity.ChildDO;
import cn.van.entity.ParentDO;
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
public class MultipleServiceTest {

    @Resource
    TestService testService;

    @Test
    public void insertChild() {
        ChildDO childDO = new ChildDO();
        childDO.setAge(12).setName("Van");
        testService.insertOne(childDO);
    }

    @Test
    public void insertParent() {
        ParentDO parentDO = new ParentDO();
        parentDO.setCId(1l).setName("妈妈");
        testService.insert(parentDO);
    }
}