package cn.van.mybatis.druid.service;


import cn.van.mybatis.druid.entity.UserDO;

import java.util.List;

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

    UserDO selectById(Long id);

    List<UserDO> selectAll();
}
