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
package cn.van.springboot.swagger3.starter.web.controller;

import cn.van.springboot.swagger3.starter.domain.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 〈一句话功能简述〉<br>
 * 〈控制器〉
 *
 * @author VanFan
 * @create 2022-02-08
 * @since 1.0.0
 */
@RestController
@RequestMapping("/swagger")
@Api(tags = "Swagger接口")
public class SwaggerController {

    /**
     * 参数是实体类的方法（需要在实体类中增加注解进行参数说明）
     * @param userDTO
     * @return
     */
    @ApiOperation(value = "实体参数方法", httpMethod = "PUT")
    @PutMapping("/entityParam")
    public UserDTO entityParam(@RequestBody UserDTO userDTO) {
        return userDTO;
    }

    /**
     * 被忽略的接口，该接口不会在Swagger上显示
     * @return
     */
    @DeleteMapping(path = "/ignore")
    @ApiIgnore(value = "这是被忽略的接口，将不会在Swagger上显示")
    public String ignoreApi() {
        return "测试";
    }

}