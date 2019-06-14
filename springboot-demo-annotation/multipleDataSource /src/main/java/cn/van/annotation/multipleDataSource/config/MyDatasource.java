package cn.van.annotation.multipleDataSource.config;

import cn.van.annotation.multipleDataSource.enums.DBEnum;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

//自定义实现类 继承AbstractRoutingDataSource(动态数据源) 来动态实现根据请求不同达到切换数据源的需求
public class MyDatasource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
//      根据MyThreadLocal.getLocal()判断该次请求使用的是哪个数据源
        DBEnum local = MyThreadLocal.getLocal();
        return local;
    }

    /**
     * 本地线程副本变量工具类
     * 将私有线程和该线程存放的副本对象做一个映射
     */
    public static class MyThreadLocal {
        private static ThreadLocal<DBEnum> local = new ThreadLocal();

        public static DBEnum getLocal() {
            return local.get();
        }

        public static void setLocal(DBEnum local) {
            MyThreadLocal.local.set(local);
        }
        public static void removeLocal() {
            MyThreadLocal.local.remove();
        }
    }
}
