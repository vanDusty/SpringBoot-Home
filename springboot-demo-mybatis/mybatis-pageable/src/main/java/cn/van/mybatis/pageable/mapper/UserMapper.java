package cn.van.mybatis.pageable.mapper;

import cn.van.mybatis.pageable.entity.UserInfoDO;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserMapper
 *
 * @author: Van
 * Date:     2019-07-24 15:52
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public interface UserMapper {

    List<UserInfoDO> findAll();

}
