package cn.van.singleton.demo.mode;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: StaticInnerClassMode
 *
 * @author: Van
 * Date:     2019-09-01 15:11
 * Description: 静态内部类
 * Version： V1.0
 */
public class StaticInnerClassMode {

    private StaticInnerClassMode(){

    }

    /**
     * 单例持有者
     */
    private static class InstanceHolder{
        private  final static StaticInnerClassMode instance = new StaticInnerClassMode();

    }

    /**
     * 提供公开获取实例接口
     * @return
     */
    public static StaticInnerClassMode getInstance(){
        // 调用内部类属性
        return InstanceHolder.instance;
    }
}