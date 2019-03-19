/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: MultipleDataSource
 * Author:   zhangfan
 * Date:     2019-03-19 14:39
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-03-19
 * @since 1.0.0
 */
public class MultipleDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }
}