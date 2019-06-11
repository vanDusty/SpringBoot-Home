/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: WebLogTestController
 * Author:   zhangfan
 * Date:     2019-06-11 17:42
 * Description: 测试控制器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.annotation.web.controller;

import cn.van.annotation.annotation.WebLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br> 
 * 〈测试控制器〉
 *
 * @author zhangfan
 * @create 2019-06-11
 * @since 1.0.0
 */
@RestController
@RequestMapping("/")
public class WebLogTestController {

    /**
     * 一般请求，不需打印日志
     * @return
     */
    @GetMapping("/sayHello")
    public String sayHello() {
        return "Hello";
    }

    /**
     * 需要打印日志的请求
     * @param str
     * @return
     */
    @GetMapping("/webLog")
    @WebLog(description = "测试一下自定义注解")
    public String webLogTest(String str) {
        return "成功返回:" + str;
    }
}