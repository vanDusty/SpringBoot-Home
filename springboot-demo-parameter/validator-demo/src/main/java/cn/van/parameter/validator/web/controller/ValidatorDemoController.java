package cn.van.parameter.validator.web.controller;

import cn.van.parameter.validator.DTO.UserDTO;
import cn.van.parameter.validator.domain.HttpResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ValidatorController
 *
 * @author: Van
 * Date:     2019-05-09 20:41
 * Description: 注解校验
 * Version： V1.0
 */
@RestController
@RequestMapping("/demo")
public class ValidatorDemoController {

    /**
     * 注解参数校验案例
     * @param userDTO
     * @return
     */
    @PostMapping("/test")
    public HttpResult test(@Validated UserDTO userDTO) {
        return HttpResult.success(userDTO);
    }

}
