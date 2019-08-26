package cn.van.redis.view.web.controller;

import cn.van.redis.view.annotation.PageView;
import cn.van.redis.view.entity.ArticleDO;
import cn.van.redis.view.service.ArticleService;
import cn.van.redis.view.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: PageController
 *
 * @author: Van
 * Date:     2019-03-26 19:17
 * Description:
 * Version： V1.0
 */
@RestController
@Slf4j
public class PageController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisUtils redisUtil;

    /**
     * 访问一篇文章时，增加其浏览量:重点在的注解
     * @param articleId：文章id
     * @return
     */
    @PageView
    @RequestMapping("/{articleId}")
    public String getArticle(@PathVariable("articleId") Long articleId) {
        try{
            ArticleDO blog = articleService.getById(articleId);
            log.info("articleId = {}", articleId);
            String key = "articleId_"+articleId;
            Long view = redisUtil.size(key);
            log.info("redis 缓存中浏览数：{}", view);
            //直接从缓存中获取并与之前的数量相加
            Long views = view + blog.getViews();
            log.info("文章总浏览数：{}", views);
        } catch (Throwable e) {
            return  "error";
        }
        return  "success";
    }
}