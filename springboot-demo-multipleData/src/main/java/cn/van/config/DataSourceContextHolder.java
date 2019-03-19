/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: DataSourceContextHolder
 * Author:   zhangfan
 * Date:     2019-03-19 14:38
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈保存当前线程使用的数据源名〉<br>
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-03-19
 * @since 1.0.0
 */
public class DataSourceContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceContextHolder.class);

    private static final ThreadLocal<String> contextHolder = new InheritableThreadLocal<>();

    /**
     *  设置数据源
     * @param db
     */
    public static void setDataSource(String db){
        logger.debug("切换到{}数据源", db);
        contextHolder.set(db);
    }

    /**
     * 取得当前数据源
     * @return
     */
    public static String getDataSource(){
        return contextHolder.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clear(){
        contextHolder.remove();
    }
}