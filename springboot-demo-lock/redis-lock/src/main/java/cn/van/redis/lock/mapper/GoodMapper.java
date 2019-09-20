package cn.van.redis.lock.mapper;

import cn.van.redis.lock.entity.GoodDO;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: GoodMapper
 *
 * @author: Van
 * Date:     2019-09-20 00:47
 * Description:
 * Version： V1.0
 */
public interface GoodMapper {

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    GoodDO selectByPrimaryKey(Long id);

    /**
     * 售卖一件商品（库存减少1）
     * @param id
     * @return
     */
    int saleOneGood(Long id);
}