/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: ExcelData
 * Author:   zhangfan
 * Date:     2019-03-29 11:11
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.excel.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-03-29
 * @since 1.0.0
 */
@Data
public class ExcelData implements Serializable {

    private static final long serialVersionUID = 4444017239100620999L;

    // 表头
    private List<String> tableHead;

    // 数据
    private List<List<Object>> data;

    // 页签名称
    private String tagName;
}

