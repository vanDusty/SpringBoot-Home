/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: UserInfoService
 * Author:   zhangfan
 * Date:     2019-04-26 16:32
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.shiro.service;

import cn.van.shiro.entity.UserInfo;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-26
 * @since 1.0.0
 */
public interface UserInfoService {

    UserInfo findByUsername(String username);
}