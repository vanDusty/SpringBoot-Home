package cn.van.redis.view.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class ArticleDO implements Serializable {
    private Long id;

    private String title;

    private String content;

    private String url;

    private Long views;

    private Date createTime;
}