/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: TestService
 * Author:   zhangfan
 * Date:     2019-04-19 16:14
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.dubbo.service;

import cn.van.dubbo.domain.User;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-19
 * @since 1.0.0
 */
public interface TestService {

    String sayHello(String str);

    User findUser();
}