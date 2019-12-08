package cn.van.easyexcel.export.web.controller;

import cn.van.easyexcel.export.model.ImportModel;
import cn.van.easyexcel.export.service.ExportExcelService;
import cn.van.easyexcel.export.service.ReadExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
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
 * Description: 读取 excel
 * Version： V1.0
 */
@RestController
@Api(tags = "EasyExcel 读取")
@RequestMapping("/import")
public class ReadExcelController {

    @Resource
    ReadExcelService readExcelService;


    @ApiOperation(value = "读取 整个Excel(多个 sheet 需要 各个 sheet 字段相同)")
    @PostMapping(value = "/readAllExcel")
    public List<ImportModel> readAllExcel(MultipartFile excel) {
        return readExcelService.readExcel(excel, new ImportModel());
    }

    @ApiImplicitParam( name = "sheetNo", value = "读第几个表单", required = true)
    @ApiOperation(value = "读取 Excel 的指定 sheet 全部数据")
    @PostMapping(value = "/readOneSheet")
    public List<ImportModel> readOneSheet(MultipartFile excel,
                                          @RequestParam Integer sheetNo) {
        return readExcelService.readExcel(excel, new ImportModel(), sheetNo);
    }

    @ApiImplicitParams({
            @ApiImplicitParam( name = "sheetNo", value = "读第几个表单", required = true),
            @ApiImplicitParam( name = "headLineNum", value = "从第几行数据开始读", required = true)
    })
    @ApiOperation(value = "读取 Excel 的指定 sheet 指定数据")
    @PostMapping(value = "/readExcel")
    public List<ImportModel> readExcel(MultipartFile excel,
                                       @RequestParam Integer sheetNo,
                                       @RequestParam Integer headLineNum) {
        return readExcelService.readExcel(excel, new ImportModel(), sheetNo,headLineNum);
    }



}
