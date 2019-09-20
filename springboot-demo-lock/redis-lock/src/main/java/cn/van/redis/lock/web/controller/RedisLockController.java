package cn.van.redis.lock.web.controller;

import cn.van.redis.lock.service.RedisLockService;
import cn.van.redis.lock.util.HttpResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: RedisLockController
 *
 * @author: Van
 * Date:     2019-09-17 19:40
 * Description: 测试控制器
 * Version： V1.0
 */
@RestController
@RequestMapping("/redisLock")
public class RedisLockController {


    @Resource
    RedisLockService redisLockService;


    /**
     * 家庭成员领取奖励（不加锁）
     * @return
     */
    @PostMapping("/receiveAward")
    public HttpResult receiveAward() {
        return redisLockService.receiveAward();
    }

    /**
     * 家庭成员领取奖励（加锁）
     * @return
     */
    @PostMapping("/receiveAwardLock")
    public HttpResult receiveAwardLock() {
        return redisLockService.receiveAwardLock();
    }

    /**
     * 售卖商品(不加锁)
     * @return
     */
    @PostMapping("/saleGoods")
    public HttpResult saleGoods() {
        return redisLockService.saleGoods();
    }
    /**
     * 售卖商品(加锁)
     * @return
     */
    @PostMapping("/saleGoodsLock")
    public HttpResult saleGoodsLock() {
        return redisLockService.saleGoodsLock();
    }


}
