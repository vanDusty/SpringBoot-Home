package cn.van.easyexcel.one.service;


import javax.servlet.http.HttpServletResponse;

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
public interface ExportExcelService {

    /**
     * 导出excel(单表单)
     * @param response
     */
    void exportWithOneSheet(HttpServletResponse response);

    /**
     * 导出excel(多个表单)
     * @param response
     */
    void exportWithSheets(HttpServletResponse response);


    /**
     * 异步导出excel(单表单)
     * @return 上传后的链接
     */
    String asyExportWithOneSheet();

}
