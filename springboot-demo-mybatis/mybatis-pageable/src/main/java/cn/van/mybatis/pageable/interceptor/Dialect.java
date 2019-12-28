package cn.van.mybatis.pageable.interceptor;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: Dialect
 *
 * @author: Van
 * Date:     2019-12-28 19:05
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public interface Dialect {
    /**
     * 获取countSQL语句
     * @param targetSql
     * @return
     */
    default String getCountSql(String targetSql){
        return String.format("select count(1) from (%s) tmp_count",targetSql);
    }

    String getLimitSql(String targetSql, int offset, int limit);
}
