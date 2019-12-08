package cn.van.easyexcel.export.util;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ExcelWriterFactory
 *
 * @author: Van
 * Date:     2019-10-11 12:02
 * Description: 用于导出多个 sheet 的 Excel，通过多次调用 write 方法写入多个 sheet。
 * Version： V1.0
 */
public class ExcelWriterFactory extends ExcelWriter {
    private OutputStream outputStream;
    private int sheetNo = 1;

    public ExcelWriterFactory(OutputStream outputStream, ExcelTypeEnum typeEnum) {
        super(outputStream, typeEnum);
        this.outputStream = outputStream;
    }

    public ExcelWriterFactory write(List<? extends BaseRowModel> list, String sheetName,
                                    BaseRowModel object) {
        this.sheetNo++;
        try {
            Sheet sheet = new Sheet(sheetNo, 0, object.getClass());
            sheet.setSheetName(sheetName);
            this.write(list, sheet);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public void finish() {
        super.finish();
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
