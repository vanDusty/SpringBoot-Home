package cn.van.mybatis.pageable.model;


import cn.van.mybatis.pageable.interceptor.PageInterceptor;

import java.util.List;

/**
 * @author chenmingming
 * @date 2018/9/24
 */
public class PageResult<T>{
    private long total;

    private List<T> data;

    public PageResult(List<T> data) {
        this.data = data;
        PageInterceptor.PageParm pageParm = PageInterceptor.PARM_THREAD_LOCAL.get();
        if(pageParm != null){
            total = pageParm.totalSize;
            PageInterceptor.PARM_THREAD_LOCAL.remove();
        }
    }

    public long getTotal() {
        return total;
    }

    public List<T> getData() {
        return data;
    }
}
