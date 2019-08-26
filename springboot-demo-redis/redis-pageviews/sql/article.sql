DROP TABLE IF EXISTS `article`;

CREATE TABLE `article` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                         `title` varchar(100) NOT NULL COMMENT '标题',
                         `content` varchar(1024) NOT NULL COMMENT '内容',
                         `url` varchar(100) NOT NULL COMMENT '地址',
                         `views` bigint(20) NOT NULL COMMENT '浏览量',
                         `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO article VALUES(1,'测试文章','content','url',10,NULL);