package cn.van.easyexcel.one.service.impl;


import cn.van.easyexcel.one.model.ImportModel;
import cn.van.easyexcel.one.service.ReadExcelService;
import cn.van.easyexcel.one.util.ImportExcelUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
