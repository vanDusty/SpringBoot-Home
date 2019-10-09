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
package cn.van.swagger.demo.web.controller;

import cn.van.swagger.demo.domain.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
@Api(tags = "Swagger接口")
public class SwaggerController {
    /**
     *  无参方法
     * @return
     */
    @ApiOperation(value = "无参方法", httpMethod = "GET")
    @GetMapping("/sayHello")
    public String sayHello(){
        return "hello Swagger!";
    }

    /**
     * 单参方法
     * @param id
     * @return
     */
    @ApiImplicitParam( name = "id", value = "主键id")
    @ApiOperation(value = "单参方法", httpMethod = "POST")
    @PostMapping("/hasParam")
    public Long hasParam(Long id) {
        return id;
    }

    /**
     * 多参方法(required = true可指定某个参数为必填)
     * @param id
     * @param userName
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam( name = "id", value = "主键id", required = true),
            @ApiImplicitParam( name = "userName", value = "用户名", required = true)
    })
    @ApiOperation(value = "多参方法", httpMethod = "POST")
    @PostMapping("/hasParams")
    public String hasParams(Long id, String userName) {
        return userName;
    }

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