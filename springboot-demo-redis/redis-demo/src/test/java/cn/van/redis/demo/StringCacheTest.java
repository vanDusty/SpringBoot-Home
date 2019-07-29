package cn.van.redis.demo; /**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: cn.van.redis.demo.StringCacheTest
 * Author:   zhangfan
 * Date:     2019-03-22 11:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

import cn.van.redis.demo.utils.StringCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StringCacheTest {

    private static final Logger logger = LoggerFactory.getLogger(StringCacheTest.class);

    @Autowired
    private StringCache stringCache;

    @Test
    public void setAndGet() {
        stringCache.setValue("name","redis测试");
        String name = stringCache.getValue("name");
        logger.info(name);
        stringCache.delKey("name");
        name = stringCache.getValue("name");
        logger.info(name);
    }

    @Test
    public void getRemainingTime() {
        stringCache.setValue("hello","hello word", 40);
        logger.info("剩余存活时间:{}秒",stringCache.getRemainingTime("hello"));
    }


    @Test
    public void exist() {
        boolean i = stringCache.existKey("hello");
        if (i) {
            logger.info("该键还存在");
            logger.info("剩余存活时间:{}秒",stringCache.getRemainingTime("hello"));
        }else {
            logger.info("该键已过期");
        }
    }

}