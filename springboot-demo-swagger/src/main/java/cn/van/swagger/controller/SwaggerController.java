/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: SwaggerController
 * Author:   zhangfan
 * Date:     2019-04-08 16:23
 * Description: 控制器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.swagger.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 〈一句话功能简述〉<br> 
 * 〈控制器〉
 *
 * @author zhangfan
 * @create 2019-04-08
 * @since 1.0.0
 */
@Api(value = "Swagger测试", tags = "测试swagger配置")
@RestController
@RequestMapping("/test")
public class SwaggerController {
    /**
     * value: 表示描述
     * httpMethod: 支持的请求方式 // 可省略
     * response: 返回的自定义的实体类 // 可省略
     * @return
     */
    @ApiOperation(value = "sayHello", httpMethod = "GET")
    @GetMapping("/sayHello")
    public String sayHello(){
        return "hello Swagger!";
    }
}