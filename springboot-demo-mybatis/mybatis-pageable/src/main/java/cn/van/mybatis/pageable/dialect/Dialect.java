package cn.van.mybatis.pageable.dialect;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: Dialect
 *
 * @author: Van
 * Date:     2019-12-28 19:05
 * Description: 数据库方言
 * Version： V1.0
 */
public interface Dialect {
    /**
     * 获取count SQL语句
     *
     * @param targetSql
     * @return
     */
    default String getCountSql(String targetSql) {
        return String.format("select count(1) from (%s) tmp_count", targetSql);
    }

    /**
     * 获取limit SQL语句
     * @param targetSql
     * @param offset
     * @param limit
     * @return
     */
    String getLimitSql(String targetSql, int offset, int limit);
}
