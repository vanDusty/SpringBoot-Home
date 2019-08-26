package cn.van.redis.view.task;

import cn.van.redis.view.entity.ArticleDO;
import cn.van.redis.view.service.ArticleService;
import cn.van.redis.view.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ArticleViewTask
 *
 * @author: Van
 * Date:     2019-03-26 22:13
 * Description: 定时将缓存的访问量更新到数据库
 * Version： V1.0
 */
@Component
@Slf4j
public class ArticleViewTask {

    @Resource
    private RedisUtils redisUtil;
    @Resource
    ArticleService articleService;


    @Scheduled(cron = "0 55 23 * * ? ")
    @Transactional(rollbackFor=Exception.class)
    public void createHyperLog() {
        log.info("浏览量入库开始");

        List<Long> list = articleService.getAllArticleId();
        list.forEach(articleId ->{
            // 获取每一篇文章在redis中的浏览量，存入到数据库中
            String key  = "articleId_"+articleId;
            Long view = redisUtil.size(key);
            if(view>0){
                ArticleDO articleDO = articleService.getById(articleId);
                Long views = view + articleDO.getViews();
                articleDO.setViews(views);
                int num = articleService.updateArticleById(articleDO);
                if (num != 0) {
                    log.info("数据库更新后的浏览量为：{}", views);
                    redisUtil.del(key);
                }
            }
        });
        log.info("浏览量入库结束");
    }

}
