package cn.van.redis.lock.service;

import cn.van.redis.lock.util.HttpResult;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: RedisLockService
 *
 * @author: Van
 * Date:     2019-09-17 19:41
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public interface RedisLockService {

    /**
     * 家庭成员领取奖励（加锁）
     * @return
     */
    HttpResult receiveAwardLock();

    /**
     * 家庭成员领取奖励（不加锁）
     * @return
     */
    HttpResult receiveAward();

    /**
     * 售卖商品（加锁）
     * @return
     */
    HttpResult saleGoods();


    /**
     * 售卖商品（加锁）
     * @return
     */
    HttpResult saleGoodsLock();

}
