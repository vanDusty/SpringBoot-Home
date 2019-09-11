package cn.van.parameter.validator.web.controller;

import cn.van.parameter.validator.DTO.UserDTO;
import cn.van.parameter.validator.domain.assist.Create;
import cn.van.parameter.validator.domain.HttpResult;
import cn.van.parameter.validator.domain.assist.Update;
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
 * Date:     2019-09-09 20:41
 * Description: 分组校验
 * Version： V1.0
 */
@RestController
@RequestMapping("/groups")
public class ValidatorGroupsController {

    /**
     * 更新数据，需要传入userID
     * @param userDTO
     * @return
     */
    @PostMapping("/update")
    public HttpResult updateData(@Validated(Update.class)UserDTO userDTO) {
        return HttpResult.success(userDTO);
    }
    /**
     * 新增数据，不需要传入userID
     * @param userDTO
     * @return
     */
    @PostMapping("/create")
    public HttpResult createData(@Validated(Create.class)UserDTO userDTO) {
        return HttpResult.success(userDTO);
    }

}
