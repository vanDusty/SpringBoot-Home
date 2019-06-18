/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: LockerTest
 * Author:   zhangfan
 * Date:     2019-03-22 11:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.redisLock.redisson;

import cn.van.redisLock.redisson.utils.LockUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
public class LockerTest {

    private static final Logger logger = LoggerFactory.getLogger(LockerTest.class);


    @Test
    public void testLock() {
        LockUtil.lock("hello");
        logger.info("获取了锁");
        try {
            //TODO 干事情
        } catch (Exception e) {
            //异常处理
        }finally{
            //释放锁
            LockUtil.unlock("hello");
            logger.info("释放锁");
        }
    }

}