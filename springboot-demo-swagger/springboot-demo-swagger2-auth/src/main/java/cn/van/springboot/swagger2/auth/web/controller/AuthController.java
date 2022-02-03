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
package cn.van.springboot.swagger2.auth.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈控制器〉
 *
 * @author zhangfan
 * @create 2019-04-08
 * @since 1.0.0
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "auth")
public class AuthController {

    @ApiImplicitParam( name = "id", value = "主键id")
    @ApiOperation(value = "不带token的方法", httpMethod = "POST")
    @PostMapping("/sayHello")
    public String sayHello() {
        return "sayHello";
    }

}