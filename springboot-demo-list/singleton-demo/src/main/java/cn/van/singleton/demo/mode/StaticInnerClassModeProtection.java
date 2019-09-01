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
public class StaticInnerClassModeProtection {

    private static boolean flag = false;

    private StaticInnerClassModeProtection(){
        synchronized(StaticInnerClassModeProtection.class){
            if(flag == false){
                flag = true;
            }else {
                throw new RuntimeException("实例已经存在，请通过 getInstance()方法获取！");
            }
        }
    }

    /**
     * 单例持有者
     */
    private static class InstanceHolder{
        private  final static StaticInnerClassModeProtection instance = new StaticInnerClassModeProtection();
    }

    /**
     * 提供公开获取实例接口
     * @return
     */
    public static StaticInnerClassModeProtection getInstance(){
        // 调用内部类属性
        return InstanceHolder.instance;
    }
}