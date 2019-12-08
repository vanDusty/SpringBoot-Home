package cn.van.easyexcel.export.util;

import cn.van.easyexcel.export.expection.ExcelException;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ImportExcelUtil
 *
 * @author: Van
 * Date:     2019-10-11 21:55
 * Description: 读取 excel 工具类
 * Version： V1.0
 */
public class ImportExcelUtil {

    /**
     * 读取 整个Excel(多个 sheet 需要 各个 sheet 字段相同)
     *
     * @param excel    文件
     * @param baseRowModel 实体类映射，继承 BaseRowModel 类
     * @return Excel 数据 list
     */
    public static List readExcel(MultipartFile excel, BaseRowModel baseRowModel) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReader reader = getReader(excel, excelListener);
        if (reader == null) {
            return null;
        }
        for (Sheet sheet : reader.getSheets()) {
            if (baseRowModel != null) {
                sheet.setClazz(baseRowModel.getClass());
            }
            reader.read(sheet);
        }
        return excelListener.getDatas();
    }

    /**
     * 读取 Excel 的指定 sheet 指定数据
     *
     * @param excel       文件
     * @param baseRowModel    实体类映射，继承 BaseRowModel 类
     * @param sheetNo     sheet 的序号（从第几个 sheet 开始读）
     * @param headLineNum 表头行数（从第几行开始读）
     * @return Excel 数据 list
     */
    public static List readExcel(MultipartFile excel, BaseRowModel baseRowModel,
                                         int sheetNo, int headLineNum) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReader reader = getReader(excel, excelListener);
        if (reader == null) {
            return null;
        }
        reader.read(new Sheet(sheetNo, headLineNum, baseRowModel.getClass()));
        return excelListener.getDatas();
    }

    /**
     * 读取 Excel 的指定 sheet 全部数据
     *
     * @param excel       文件
     * @param baseRowModel    实体类映射，继承 BaseRowModel 类
     * @param sheetNo     sheet 的序号（从第几个 sheet 开始读）
     * @return Excel 数据 list
     */
    public static List readExcel(MultipartFile excel, BaseRowModel baseRowModel, int sheetNo) {
        return readExcel(excel, baseRowModel, sheetNo, 1);
    }

    /**
     * 返回 ExcelReader
     *
     * @param excel         需要解析的 Excel 文件
     * @param excelListener new ExcelListener()
     */
    private static ExcelReader getReader(MultipartFile excel, ExcelListener excelListener) {
        String filename = excel.getOriginalFilename();
        if (filename == null) {
            throw new ExcelException("文件格式错误！");
        }
        if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
            throw new ExcelException("文件格式错误！");
        }
        InputStream inputStream;
        try {
            inputStream = new BufferedInputStream(excel.getInputStream());
            return new ExcelReader(inputStream, null, excelListener, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
