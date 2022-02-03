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
import org.springframework.web.bind.annotation.*;
/**
 * 〈一句话功能简述〉<br>
 * 〈控制器〉
 *
 * @author zhangfan
 * @create 2019-04-08
 * @since 1.0.0
 */
@RestController
@RequestMapping("/swagger")
@Api(tags = "token")
public class TokenController {

    @ApiOperation(value = "一般方法", httpMethod = "GET")
    @GetMapping("/common")
    public String common(){
        return "common";
    }

}