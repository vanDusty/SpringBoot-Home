package cn.van.easyexcel.export.service.impl;

import cn.van.easyexcel.export.model.ExportModel;
import cn.van.easyexcel.export.service.ExportExcelService;
import cn.van.easyexcel.export.util.ExportExcelUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ExportExcelServiceImpl
 *
 * @author: Van
 * Date:     2019-10-17 16:33
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Service
public class ExportExcelServiceImpl implements ExportExcelService {

    @Override
    public void exportWithOneSheet(HttpServletResponse response) {
        List<ExportModel> list = getList();
        String fileName = createFileName();
        ExportExcelUtil.writeExcel(response, list, fileName,"第一个表单",new ExportModel());
    }

    @Override
    public void exportWithSheets(HttpServletResponse response) {
        List<ExportModel> first = getList();
        List<ExportModel> second = getAnotherList();
        String fileName = createFileName();
        ExportExcelUtil.writeExcelWithSheets(response, first, fileName, "第一个表单", new ExportModel())
                .write(second, "第二个表单", new ExportModel())
                .finish();
    }

    @Override
    public String asyExportWithOneSheet() {
        List<ExportModel> list = getList();
        return ExportExcelUtil.asyWriteExcel(list,"第一个表单",new ExportModel());
    }

    /**
     * 造几条导出的数据（实际情况：从数据库查询动态数据）
     * @return
     */
    public static List<ExportModel> getList() {
        List<ExportModel> list = new ArrayList<>();
        ExportModel model1 = new ExportModel("张三", "高三", "语文", 130);
        ExportModel model2 = new ExportModel("张三", "高三", "数学", 140);
        ExportModel model3 = new ExportModel("张三", "高三", "英语", 125);
        ExportModel model4 = new ExportModel("张三", "高三", "化学", 90);
        list.add(model1);
        list.add(model2);
        list.add(model3);
        list.add(model4);
        return list;
    }
    public static List<ExportModel> getAnotherList() {
        List<ExportModel> list = new ArrayList<>();
        ExportModel model1 = new ExportModel("李四", "高二", "语文", 120);
        ExportModel model2 = new ExportModel("李四", "高二", "数学", 125);
        ExportModel model3 = new ExportModel("李四", "高二", "英语", 140);
        ExportModel model4 = new ExportModel("李四", "高二", "化学", 85);
        list.add(model1);
        list.add(model2);
        list.add(model3);
        list.add(model4);
        return list;
    }

    /**
     * 用当前时间生成文件名
     * @return
     */
    public static String createFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }
}
