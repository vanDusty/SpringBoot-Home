package cn.van.redis.view.service.impl;

import cn.van.redis.view.annotation.PageView;
import cn.van.redis.view.entity.ArticleDO;
import cn.van.redis.view.mapper.ArticleMapper;
import cn.van.redis.view.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: BlogServiceImpl
 *
 * @author: Van
 * Date:     2019-03-26 19:32
 * Description:
 * Version： V1.0
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    ArticleMapper articleMapper;




    @Override
    public ArticleDO getById(Long id) {
        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Long> getAllArticleId() {
        return articleMapper.selectAllArticleId();
    }

    @Override
    public int updateArticleById(ArticleDO articleDO) {
        return articleMapper.updateByPrimaryKey(articleDO);
    }
}