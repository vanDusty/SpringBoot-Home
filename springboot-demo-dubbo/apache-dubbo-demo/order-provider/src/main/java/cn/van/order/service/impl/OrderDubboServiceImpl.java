/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: TestServiceImpl
 * Author:   zhangfan
 * Date:     2019-04-19 16:21
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.order.service.impl;

import cn.van.order.service.OrderDubboService;
import cn.van.order.domain.OrderDomain;
import org.apache.dubbo.config.annotation.Service;

import java.time.LocalDateTime;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-19
 * @since 1.0.0
 */
@Service(version = "1.0.0")
public class OrderDubboServiceImpl implements OrderDubboService {


    @Override
    public OrderDomain getOrder() {
        return new OrderDomain(1, 10086, LocalDateTime.now());
    }
}