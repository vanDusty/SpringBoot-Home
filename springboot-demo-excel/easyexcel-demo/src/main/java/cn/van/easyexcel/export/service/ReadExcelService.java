package cn.van.easyexcel.export.service;


import cn.van.easyexcel.export.model.ImportModel;
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
     * 读取 整个Excel(多个 sheet 需要 各个 sheet 字段相同)
     *
     * @param excel    文件
     * @param importModel 实体类映射，继承 BaseRowModel 类
     * @return Excel 数据 list
     */
    List<ImportModel> readExcel(MultipartFile excel, ImportModel importModel);

    /**
     * 读取 Excel 的指定 sheet 指定数据
     *
     * @param excel       文件
     * @param importModel 实体类映射，继承 BaseRowModel 类
     * @param sheetNo     sheet 的序号（从第几个 sheet 开始读）
     * @param headLineNum 表头行数（从第几行开始读）
     * @return Excel 数据 list
     */
    List<ImportModel> readExcel(MultipartFile excel, ImportModel importModel, int sheetNo,
                                int headLineNum);

    /**
     * 读取 Excel 的指定 sheet 全部数据
     *
     * @param excel       文件
     * @param importModel 实体类映射，继承 BaseRowModel 类
     * @param sheetNo     sheet 的序号（从第几个 sheet 开始读）
     * @return Excel 数据 list
     */
    List<ImportModel> readExcel(MultipartFile excel, ImportModel importModel, int sheetNo);
}
