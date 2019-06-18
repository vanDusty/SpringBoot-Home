/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: StringCacheTest
 * Author:   zhangfan
 * Date:     2019-03-22 11:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.redisLock.redisson;

import cn.van.redisLock.redisson.utils.RedissLockUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-03-22
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class StringCacheTest {

    private static final Logger logger = LoggerFactory.getLogger(StringCacheTest.class);

    @Autowired
    private RedissLockUtil redissLockUtil;

    @Test
    public void setAndGet() {
        try {
            System.out.println("ddddd");
            redissLockUtil.lock("hello1", TimeUnit.SECONDS,3l);
            System.out.println("hello1");
            Thread.sleep(3000);
            System.out.printf("hello1");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
//            redissLockUtil.unlock("hello");
        }
    }

    @Test
    public void getRemainingTime() {
        System.out.println("hello1");
        redissLockUtil.lock("hello1");
        System.out.println("hello1");
        redissLockUtil.unlock("hello1");
        System.out.printf("hello1");
    }


    @Test
    public void exist() {

    }

}