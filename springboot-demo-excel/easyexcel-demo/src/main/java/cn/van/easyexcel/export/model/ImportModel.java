package cn.van.easyexcel.export.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ExportModel
 *
 * @author: Van
 * Date:     2019-10-11 14:18
 * Description: 读取对象模版，必须要继承自 BaseRowModel.java
 * Version： V1.0
 */
@Data
public class ImportModel extends BaseRowModel {

    /**
     * 通过 @ExcelProperty 的value 指定每个字段的列名称，index 为列的序号。
     */
    @ExcelProperty(value = "姓名", index = 0)
    private String studentName;

    @ExcelProperty(value = "年级", index = 1)
    private String grade;

    @ExcelProperty(value = "学科", index = 2)
    private String subject;

    @ExcelProperty(value = "分数", index = 3)
    private Integer fraction;

    public ImportModel() {

    }
}
