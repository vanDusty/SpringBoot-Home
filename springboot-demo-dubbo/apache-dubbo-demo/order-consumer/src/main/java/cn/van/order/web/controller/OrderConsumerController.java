/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: TestController
 * Author:   zhangfan
 * Date:     2019-04-19 16:24
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.order.web.controller;

import cn.van.order.result.RpcResult;
import cn.van.order.service.OrderDubboService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-19
 * @since 1.0.0
 */
@RestController
@RequestMapping("/order")
public class OrderConsumerController {
    @Reference
    OrderDubboService orderDubboService;

    @GetMapping("getOrder")
    public RpcResult getOrder() {
        return orderDubboService.getOrder();
    }
}