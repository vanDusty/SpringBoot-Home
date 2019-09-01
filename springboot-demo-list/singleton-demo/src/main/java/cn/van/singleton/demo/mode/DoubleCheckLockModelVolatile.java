package cn.van.singleton.demo.mode;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: DoubleCheckLockModelVolatile
 *
 * @author: Van
 * Date:     2019-09-01 15:10
 * Description: 双重检查锁模式优化
 * Version： V1.0
 */
public class DoubleCheckLockModelVolatile {
    /**
     * 添加volatile关键字，保证在读操作前，写操作必须全部完成
     */
    private static volatile DoubleCheckLockModelVolatile instance;
    /**
     * 私有化构造函数
     */
    private DoubleCheckLockModelVolatile(){

    }
    /**
     * 提供公开获取实例接口
     * @return
     */
    public static DoubleCheckLockModelVolatile getInstance(){

        if (instance == null) {
            synchronized (DoubleCheckLockModelVolatile.class) {
                if (instance == null) {
                    instance = new DoubleCheckLockModelVolatile();
                }
            }
        }
        return instance;
    }
}
