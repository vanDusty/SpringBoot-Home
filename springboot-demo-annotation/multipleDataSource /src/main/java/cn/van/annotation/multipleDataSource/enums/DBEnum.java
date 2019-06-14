/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: DBEnum
 * Author:   zhangfan
 * Date:     2019-06-14 17:02
 * Description: 数据库枚举类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.annotation.multipleDataSource.enums;

/**
 * 〈〉<br>
 * 〈数据库枚举类〉
 *
 * @author zhangfan
 * @create 2019-06-14
 * @since 1.0.0
 */
public enum  DBEnum {
    MASTER("Master", 1),
    SLAVES("Slave",2),
    WeiBo("weibo",3),
    QQ("qq",4),

    ;
    private String name;
    private int code;

    DBEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}