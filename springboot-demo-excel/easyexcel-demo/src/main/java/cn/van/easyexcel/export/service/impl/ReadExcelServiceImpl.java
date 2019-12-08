package cn.van.easyexcel.export.service.impl;


import cn.van.easyexcel.export.expection.ExcelException;
import cn.van.easyexcel.export.model.ImportModel;
import cn.van.easyexcel.export.service.ReadExcelService;
import cn.van.easyexcel.export.util.ExcelListener;
import cn.van.easyexcel.export.util.ImportExcelUtil;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
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
@Service
public class ReadExcelServiceImpl implements ReadExcelService {

    @Override
    public List<ImportModel> readExcel(MultipartFile excel, ImportModel importModel) {
        return ImportExcelUtil.readExcel(excel, importModel);
    }

    @Override
    public List<ImportModel> readExcel(MultipartFile excel, ImportModel importModel, int sheetNo,
                                         int headLineNum) {
        return ImportExcelUtil.readExcel(excel, importModel, sheetNo, headLineNum);
    }

    @Override
    public List<ImportModel> readExcel(MultipartFile excel, ImportModel importModel, int sheetNo) {
        return ImportExcelUtil.readExcel(excel, importModel, sheetNo);
    }


}
