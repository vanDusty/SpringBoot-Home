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
package cn.van.dubbo.controller;

import cn.van.dubbo.domain.UserDomain;
import cn.van.dubbo.service.DubboService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-19
 * @since 1.0.0
 */
@RestController
@RequestMapping("/consumer")
public class DubboTestController {

    @Resource
    private DubboService dubboService;

    @GetMapping("/getInfo")
    public UserDomain user() {
        return dubboService.findUser();
    }
}