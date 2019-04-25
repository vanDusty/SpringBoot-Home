/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: TestService
 * Author:   zhangfan
 * Date:     2019-04-25 10:42
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.mybatis.service;

import cn.van.mybatis.entity.User;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-25
 * @since 1.0.0
 */
public interface TestService {
    // 插入主库
    void insertMater(User user);
    // 插入从库
    void insert(User user);
}