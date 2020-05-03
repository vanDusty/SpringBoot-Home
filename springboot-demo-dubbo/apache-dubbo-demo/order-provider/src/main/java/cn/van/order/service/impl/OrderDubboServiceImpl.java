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

import cn.van.order.result.RpcResult;
import cn.van.order.service.OrderDubboService;
import cn.van.order.domain.OrderDomain;
import org.apache.dubbo.config.annotation.Service;

import java.time.LocalDateTime;

/**
 * @公众号： 风尘博客
 * @Classname ResultCodeEnum
 * @Description 服务实现接口
 * @Date 2020/4/30 8:55 下午
 * @Author by Van
 */
@Service
public class OrderDubboServiceImpl implements OrderDubboService {

    @Override
    public RpcResult<OrderDomain> getOrder() {
        return RpcResult.success(new OrderDomain(1, 10086, LocalDateTime.now()));
    }
}