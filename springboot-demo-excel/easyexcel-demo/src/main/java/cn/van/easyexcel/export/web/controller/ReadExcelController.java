package cn.van.easyexcel.export.web.controller;

import cn.van.easyexcel.export.model.ImportModel;
import cn.van.easyexcel.export.service.ExportExcelService;
import cn.van.easyexcel.export.service.ReadExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ExcelController
 *
 * @author: Van
 * Date:     2019-10-11 14:15
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@RestController
@Api(tags = "EasyExcel 读取")
@RequestMapping("/read")
public class ReadExcelController {

    @Resource
    ReadExcelService readExcelService;

    /**
     * 读取 Excel（一个 sheet）
     * @param
     * @throws IOException
     */
    @ApiOperation(value = "读取 Excel")
    @PostMapping(value = "/readExcel")
    public List<Object> readExcel(MultipartFile excel) {
        return readExcelService.readExcel(excel, new ImportModel());
    }

    /**
     * 导出 Excel（多个 sheet）
     */
    @ApiOperation(value = "读取 Excel（指定 sheet）")
    @PostMapping(value = "/read")
    public List<Object> read(MultipartFile excel) {
        return readExcelService.readExcel(excel, new ImportModel(), 2,4);
    }

}
