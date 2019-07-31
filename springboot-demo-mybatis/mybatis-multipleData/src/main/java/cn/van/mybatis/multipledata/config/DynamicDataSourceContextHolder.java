package cn.van.mybatis.multipledata.config;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: DynamicDataSourceContextHolder
 *
 * @author: Van
 * Date:     2019-07-30 15:38
 * Description: 数据源上下文
 * Version： V1.0
 */
@Slf4j
public class DynamicDataSourceContextHolder {

    /**
     * 存储已经注册的数据源的key
     */
    public static List<String> keys = new ArrayList<>();

    /**
     * 线程级别的私有变量
     */
    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    public static String getDataSourceRouterKey () {
        return HOLDER.get();
    }

    public static void setDataSourceRouterKey (String key) {
        log.info("切换至{}数据源", key);
        HOLDER.set(key);
    }

    /**
     * 设置数据源之前一定要先移除
     */
    public static void removeDataSourceRouterKey () {
        HOLDER.remove();
    }

    /**
     * 判断指定DataSource当前是否存在
     *
     * @param key
     * @return
     */
    public static boolean containsDataSource(String key){
        return keys.contains(key);
    }

}
