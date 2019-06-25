package cn.van.qiniu.controller;

import cn.van.qiniu.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: QiniuCloudController
 * @author:   Van
 * Date:     2019-06-25 14:45
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@RestController
@RequestMapping("/upload")
@Api(tags = "七牛云上传接口")
public class QiNiuCloudController {
    @Resource
    UploadService uploadService;



    @ApiOperation(value = "上传图片到七牛云")
    @PostMapping(value="/uploadToQiNiu")
    public String uploadImg(@RequestParam("file") MultipartFile image) {
        return uploadService.uploadImg(image);
    }

    @ApiOperation(value = "测试接口")
    @PostMapping(value="/test")
    public String test() {
        return "test";
    }
}
