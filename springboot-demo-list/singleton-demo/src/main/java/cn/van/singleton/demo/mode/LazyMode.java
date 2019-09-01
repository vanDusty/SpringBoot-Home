package cn.van.singleton.demo.mode;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: LazyMode
 *
 * @author: Van
 * Date:     2019-09-01 14:59
 * Description: 懒汉模式
 * Version： V1.0
 */
public class LazyMode {
    /**
     * 定义静态变量时，未初始化实例
     */
    private static LazyMode instance;

    /**
     * 私有化构造函数
     */
    private LazyMode(){
        // 里面可以有很多操作
    }
    /**
     * 提供公开获取实例接口
     * @return
     */
    public static LazyMode getInstance(){
        // 使用时，先判断实例是否为空，如果实例为空，则实例化对象
        if (instance == null) {
            instance = new LazyMode();
        }
        return instance;
    }
}

