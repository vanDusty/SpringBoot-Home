package cn.van.redis.view.mapper;

import cn.van.redis.view.entity.ArticleDO;

import java.util.List;

public interface ArticleMapper {

    /**
     * 查询单篇文章
     * @param id
     * @return
     */
    ArticleDO selectByPrimaryKey(Long id);

    /**
     * 更新某篇文章
     * @param record
     * @return
     */
    int updateByPrimaryKey(ArticleDO record);

    /**
     * 查询全部文章的id
     * @return
     */
    List<Long> selectAllArticleId();
}