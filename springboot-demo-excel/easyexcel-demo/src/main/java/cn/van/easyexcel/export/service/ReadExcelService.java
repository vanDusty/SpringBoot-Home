package cn.van.easyexcel.export.service;


import com.alibaba.excel.metadata.BaseRowModel;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ExportExcelService
 *
 * @author: Van
 * Date:     2019-10-17 16:33
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public interface ReadExcelService {

    /**
     * 读取 Excel(多个 sheet)
     *
     * @param excel    文件
     * @param rowModel 实体类映射，继承 BaseRowModel 类
     * @return Excel 数据 list
     */
    List<Object> readExcel(MultipartFile excel, BaseRowModel rowModel);

    /**
     * 读取某个 sheet 的 Excel
     *
     * @param excel       文件
     * @param rowModel    实体类映射，继承 BaseRowModel 类
     * @param sheetNo     sheet 的序号 从1开始
     * @param headLineNum 表头行数，默认为1
     * @return Excel 数据 list
     */
    List<Object> readExcel(MultipartFile excel, BaseRowModel rowModel, int sheetNo,
                           int headLineNum);
}
