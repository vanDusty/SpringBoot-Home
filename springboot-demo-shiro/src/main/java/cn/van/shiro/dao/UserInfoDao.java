/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: UserInfoDao
 * Author:   zhangfan
 * Date:     2019-04-26 16:39
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.shiro.dao;

import cn.van.shiro.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-26
 * @since 1.0.0
 */
public interface UserInfoDao extends CrudRepository<UserInfo,Long> {
    /**
     * 通过username查找用户信息;
     */
    public UserInfo findByUsername(String username);
}