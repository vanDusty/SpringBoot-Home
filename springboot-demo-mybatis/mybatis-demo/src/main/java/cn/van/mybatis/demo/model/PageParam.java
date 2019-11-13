package cn.van.mybatis.demo.model;

import lombok.Data;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: PageParam
 *
 * @author: Van
 * Date:     2019-11-11 19:19
 * Description: 通用分页查询参数
 * Version： V1.0
 */
@Data
public class PageParam {
    /**
     * 当前页
     */
    private int pageNum;
    /**
     * 每页数量
     */
    private int pageSize;

    public PageParam(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

}
