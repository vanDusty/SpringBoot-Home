package cn.van.singleton.demo.mode;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: HungryMode
 *
 * @author: Van
 * Date:     2019-09-01 14:55
 * Description: 饿汉模式
 * Version： V1.0
 */
public class HungryMode {

    /**
     * 利用静态变量来存储唯一实例
     */
    private static final HungryMode instance = new HungryMode();

    /**
     * 私有化构造函数
     */
    private HungryMode(){
        // 里面可以有很多操作
    }

    /**
     * 提供公开获取实例接口
     * @return
     */
    public static HungryMode getInstance(){
        return instance;
    }
}
