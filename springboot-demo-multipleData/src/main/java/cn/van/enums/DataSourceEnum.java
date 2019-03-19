/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: DataSourceEnum
 * Author:   zhangfan
 * Date:     2019-03-19 14:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.enums;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-03-19
 * @since 1.0.0
 */
public enum  DataSourceEnum {

    DB1("master"),DB2("db2");

    private String value;

    DataSourceEnum(String value){this.value=value;}

    public String getValue() {
        return value;
    }
}