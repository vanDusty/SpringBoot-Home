package cn.van.mybatis.pageable.page;

import java.util.List;
/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: PageResult
 *
 * @author: Van
 * Date:     2019-12-28 20:05
 * Description: 分页结果
 * Version： V1.0
 */
public class PageResult<T> {
    private long total;

    private List<T> data;

    public PageResult(List<T> data) {
        this.data = data;
        PageInterceptor.PageParam pageParam = PageInterceptor.PARM_THREAD_LOCAL.get();
        if (pageParam != null) {
            total = pageParam.totalSize;
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
