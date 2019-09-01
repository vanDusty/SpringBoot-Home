package cn.van.singleton.demo.mode;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: LazyModeSynchronized
 *
 * @author: Van
 * Date:     2019-09-01 15:05
 * Description: 懒汉模式的优化-加锁
 * Version： V1.0
 */
public class LazyModeSynchronized {
    /**
     * 定义静态变量时，未初始化实例
     */
    private static LazyModeSynchronized instance;
    /**
     * 私有化构造函数
     */
    private LazyModeSynchronized(){
        // 里面可以有很多操作
    }
    /**
     * 提供公开获取实例接口
     * @return
     */
    public synchronized static LazyModeSynchronized getInstance(){
        /**
         * 添加class类锁，影响了性能，加锁之后将代码进行了串行化，
         * 我们的代码块绝大部分是读操作，在读操作的情况下，代码线程是安全的
         *
         */
        if (instance == null) {
            instance = new LazyModeSynchronized();
        }
        return instance;
    }
}
