/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: UserInfoServiceImpl
 * Author:   zhangfan
 * Date:     2019-04-26 16:38
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.shiro.service.impl;

import cn.van.shiro.dao.UserInfoDao;
import cn.van.shiro.entity.UserInfo;
import cn.van.shiro.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-26
 * @since 1.0.0
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoDao userInfoDao;

    @Override
    public UserInfo findByUsername(String username) {
        System.out.println("UserInfoServiceImpl.findByUsername()");
        return userInfoDao.findByUsername(username);
    }
}