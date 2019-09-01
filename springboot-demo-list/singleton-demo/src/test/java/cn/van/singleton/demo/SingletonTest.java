package cn.van.singleton.demo;

import cn.van.singleton.demo.mode.DoubleCheckLockMode;
import cn.van.singleton.demo.mode.StaticInnerClassMode;
import cn.van.singleton.demo.mode.StaticInnerClassModeProtection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Constructor;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: SingletonTest
 *
 * @author: Van
 * Date:     2019-09-01 17:44
 * Description: 单例模式的破坏
 * Version： V1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SingletonTest {

    /**
     * 以静态内部类实现为例
     * @throws Exception
     */
    @Test
    public void singletonTest() throws Exception {
        Constructor constructor = StaticInnerClassMode.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        StaticInnerClassMode obj1 = StaticInnerClassMode.getInstance();
        StaticInnerClassMode obj2 = StaticInnerClassMode.getInstance();
        StaticInnerClassMode obj3 = (StaticInnerClassMode) constructor.newInstance();

        System.out.println("输出结果为："+obj1.hashCode()+"," +obj2.hashCode()+","+obj3.hashCode());
    }

    /**
     * 在构造方法中进行判断，若存在则抛出RuntimeException
     * @throws Exception
     */
    @Test
    public void destroyTest() throws Exception {
        Constructor constructor = StaticInnerClassModeProtection.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        StaticInnerClassModeProtection obj1 = StaticInnerClassModeProtection.getInstance();
        StaticInnerClassModeProtection obj2 = StaticInnerClassModeProtection.getInstance();
        StaticInnerClassModeProtection obj3 = (StaticInnerClassModeProtection) constructor.newInstance();

        System.out.println("输出结果为："+obj1.hashCode()+"," +obj2.hashCode()+","+obj3.hashCode());
    }

}
