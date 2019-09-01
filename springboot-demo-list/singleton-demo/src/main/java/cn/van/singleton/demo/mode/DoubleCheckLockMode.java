package cn.van.singleton.demo.mode;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: DoubleCheckLockMode
 *
 * @author: Van
 * Date:     2019-09-01 15:06
 * Description: 双重检查锁模式
 * Version： V1.0
 */
public class DoubleCheckLockMode {

    private static DoubleCheckLockMode instance;

    /**
     * 私有化构造函数
     */
    private DoubleCheckLockMode(){

    }
    /**
     * 提供公开获取实例接口
     * @return
     */
    public static DoubleCheckLockMode getInstance(){
        // 第一次判断，如果这里为空，不进入抢锁阶段，直接返回实例
        if (instance == null) {
            synchronized (DoubleCheckLockMode.class) {
                // 抢到锁之后再次判断是否为空
                if (instance == null) {
                    instance = new DoubleCheckLockMode();
                }
            }
        }
        return instance;
    }
}

