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
package cn.van.order.service;

import cn.van.order.domain.OrderDomain;
import cn.van.order.result.RpcResult;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-19
 * @since 1.0.0
 */
public interface OrderDubboService {
    RpcResult<OrderDomain> getOrder();
}