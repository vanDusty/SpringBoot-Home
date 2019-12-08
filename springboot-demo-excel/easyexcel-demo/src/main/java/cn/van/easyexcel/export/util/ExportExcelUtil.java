package cn.van.easyexcel.export.util;

import cn.van.easyexcel.export.expection.ExcelException;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ExportExcelUtil
 *
 * @author: Van
 * Date:     2019-10-11 11:55
 * Description: 导出工具类
 * Version： V1.0
 */
public class ExportExcelUtil {

    /**
     * 导出 Excel ：一个 sheet，带表头
     *
     * @param response  HttpServletResponse
     * @param list      需要导出的数据
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param object    映射实体类，Excel 模型
     */
    public static void writeExcel(HttpServletResponse response, List<? extends BaseRowModel> list,
                                  String fileName, String sheetName, BaseRowModel object) {
        // WriteModel 是 写入 Excel 的数据模型对象
        ExcelWriter writer = new ExcelWriter(getOutputStream(fileName, response), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, object.getClass());
        sheet.setSheetName(sheetName);
        // 异常处理
        writer.write(list, sheet);
        writer.finish();
    }

    /**
     * 导出 Excel ：多个 sheet，带表头
     *
     * @param response  HttpServletResponse
     * @param list      需要导出的数据 list
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param object    映射实体类，Excel 模型
     */
    public static ExcelWriterFactory writeExcelWithSheets(HttpServletResponse response, List<? extends BaseRowModel> list,
                                                          String fileName, String sheetName, BaseRowModel object) {
        ExcelWriterFactory writer = new ExcelWriterFactory(getOutputStream(fileName, response), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, object.getClass());
        sheet.setSheetName(sheetName);
        writer.write(list, sheet);
        return writer;
    }

    /**
     * 异步导出 Excel ：一个 sheet，带表头
     *
     * @param
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param sheetName 导入文件的 sheet 名
     * @param sheetName 导入文件的 sheet 名
     * @param object    映射实体类，Excel 模型
     */
    public static String asyWriteExcel(List<? extends BaseRowModel> list,
                                    String sheetName, BaseRowModel object) {
        // 现将数据导出excel到本地
        try {
            String fileName = URLEncoder.encode(createFileName(), "UTF-8");
            ExcelWriter writer = new ExcelWriter(getFileOutputStream(fileName), ExcelTypeEnum.XLSX);
            Sheet sheet = new Sheet(1, 0, object.getClass());
            sheet.setSheetName(sheetName);
            writer.write(list, sheet);
            writer.finish();
            // 读取该excel,并上传到oss，返回下载链接
            // File file = readFileByLines(fileName + ".xlsx");
            // return FileUploadUtil.upload(file, fileName + ".xlsx");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("创建excel失败!");
        }
        return null;
    }

    private static String createFileName() {
        Long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date()) + time;
    }

    /**
     * 导出文件时为Writer生成OutputStream
     */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) {
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName  + ".xlsx");
            return response.getOutputStream();
        } catch (IOException e) {
            throw new ExcelException("创建文件失败！");
        }
    }

    /**
     * 导出文件时为File生成OutputStream
     */
    private static OutputStream getFileOutputStream(String fileName) {
        try {
            String filePath = fileName + ".xlsx";
            File dbfFile = new File(filePath);
            if (!dbfFile.exists() || dbfFile.isDirectory()) {
                dbfFile.createNewFile();
            }
            OutputStream out = new FileOutputStream(filePath);
            return out;
        } catch (Exception e) {
            throw new RuntimeException("创建文件失败！");
        }
    }

    public static File readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                line++;
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("读取文件失败！");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    throw new RuntimeException("读取文件失败！");
                }
            }
        }
        return file;
    }
}
