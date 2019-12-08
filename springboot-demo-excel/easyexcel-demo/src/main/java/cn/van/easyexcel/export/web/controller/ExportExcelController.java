package cn.van.easyexcel.export.web.controller;

import cn.van.easyexcel.export.service.ExportExcelService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
@Api(tags = "EasyExcel 导出")
@RequestMapping("/export")
public class ExportExcelController {

    @Resource
    ExportExcelService exportExcelService;

    /**
     * 导出 Excel（一个 sheet）
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "导出 Excel", httpMethod = "GET")
    @GetMapping(value = "/exportWithOneSheet")
    public void exportWithOneSheet(HttpServletResponse response) {
        exportExcelService.exportWithOneSheet(response);
    }

    /**
     * 导出 Excel（多个 sheet）
     */
    @ApiOperation(value = "导出 Excel（多个 sheet）", httpMethod = "GET")
    @GetMapping(value = "/exportWithSheets")
    public void exportWithSheets(HttpServletResponse response) {
        exportExcelService.exportWithSheets(response);
    }

    /**
     * 异步导出 Excel（一个 sheet）
     * @param
     * @throws IOException
     */
    @ApiOperation(value = "异步导出 Excel", httpMethod = "GET")
    @GetMapping(value = "/asyExportWithOneSheet")
    public void asyExportWithOneSheet() {
        exportExcelService.asyExportWithOneSheet();
    }

}
