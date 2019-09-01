package cn.van.singleton.demo.mode;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: EnumerationMode
 *
 * @author: Van
 * Date:     2019-09-01 15:14
 * Description: 枚举类单例模式
 * Version： V1.0
 */
public class EnumerationMode {

    private EnumerationMode(){

    }

    /**
     * 枚举类型是线程安全的，并且只会装载一次
     */
    private enum Singleton{
        INSTANCE;

        private final EnumerationMode instance;

        Singleton(){
            instance = new EnumerationMode();
        }

        private EnumerationMode getInstance(){
            return instance;
        }
    }

    public static EnumerationMode getInstance(){

        return Singleton.INSTANCE.getInstance();
    }
}

