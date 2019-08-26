package cn.van.redis.view.service;

import cn.van.redis.view.entity.ArticleDO;

import java.util.List;


public interface ArticleService {

    /**
     * 获取某篇文章
     * @param id
     * @return
     */
    ArticleDO getById(Long id);

    /**
     * 获取全部文章的id
     * @return
     */
    List<Long> getAllArticleId();


    /**
     * 更新某篇文章的浏览量
     * @param articleDO
     * @return
     */
    int updateArticleById(ArticleDO articleDO);

}
