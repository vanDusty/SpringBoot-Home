package cn.van.mybatis.demo.service;

import cn.van.mybatis.demo.entity.UserDO;
import cn.van.mybatis.demo.model.PageData;
import cn.van.mybatis.demo.model.PageParam;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserService
 *
 * @author: Van
 * Date:     2019-07-26 15:05
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public interface UserService {

    /**
     * 查询单个用户
     * @param id
     * @return
     */
    UserDO selectById(Long id);

    /**
     * 分页查询用户
     * @param pageParam
     * @return
     */
    PageData selectForPage(PageParam pageParam);
}
