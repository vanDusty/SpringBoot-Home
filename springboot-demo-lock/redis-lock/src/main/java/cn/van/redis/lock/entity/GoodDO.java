package cn.van.redis.lock.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: GoodDO
 *
 * @author: Van
 * Date:     2019-09-19 23:57
 * Description: 商品实体
 * Version： V1.0
 */
@Data
public class GoodDO implements Serializable {
    private Long id;

    private String goodName;

    private Integer goodCounts;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public GoodDO(Long id, String goodName, Integer goodCounts, Date createTime) {
        this.id = id;
        this.goodName = goodName;
        this.goodCounts = goodCounts;
        this.createTime = createTime;
    }

    public GoodDO() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public Integer getGoodCounts() {
        return goodCounts;
    }

    public void setGoodCounts(Integer goodCounts) {
        this.goodCounts = goodCounts;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}