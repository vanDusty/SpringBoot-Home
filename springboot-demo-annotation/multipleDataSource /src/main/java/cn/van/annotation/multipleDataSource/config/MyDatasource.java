package cn.van.annotation.multipleDataSource.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

//自定义实现类 继承AbstractRoutingDataSource(动态数据源) 来动态实现根据请求不同达到切换数据源的需求
public class MyDatasource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
//      根据MyThreadLocal.getLocal()判断该次请求使用的是哪个数据源
        String local = MyThreadLocal.getLocal();
        return local;
    }

    /**
     * 本地线程副本变量工具类
     * 将私有线程和该线程存放的副本对象做一个映射
     */
    public static class MyThreadLocal {
        private static ThreadLocal<String> local = new ThreadLocal();

        public static String getLocal() {
            return local.get();
        }

        public static void setLocal(String local) {
            MyThreadLocal.local.set(local);
        }
        public static void remoceLocal() {
            MyThreadLocal.local.remove();
        }
    }
}
