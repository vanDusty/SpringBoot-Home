package cn.van.mybatis.multipledata.service;

import cn.van.mybatis.multipledata.entity.User;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserService
 *
 * @author: Van
 * Date:     2019-07-30 17:43
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public interface UserService {
    /**
     * 主库（master）的全部用户数据
     * @return
     */
    List<User> selectMasterAll();

    /**
     * 从库(slave1)全部用户数据
     * @return
     */
    List<User> selectSlave1();
    /**
     * 从库(slave2)全部用户数据
     * @return
     */
    List<User> selectSlave2();

}
