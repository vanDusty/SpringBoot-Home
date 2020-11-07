package cn.van.swagger.group.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: TeacherController
 *
 * @author: Van
 * Date:     2019-04-10 20:46
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Api(tags = "教师工作")
// @Api(tags = {"教师工作", "教学管理"})
@RestController
@RequestMapping(value = "/teacher")
public class TeacherController {

    @PostMapping("/teaching")
    @ApiOperation(value = "教书", tags = "教学管理")
    public String teaching() {
        return "teaching....";
    }

    @PostMapping("/preparing")
    @ApiOperation(value = "备课")
    public String preparing() {
        return "preparing....";
    }

}
