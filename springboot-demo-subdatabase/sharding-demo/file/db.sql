DROP TABLE IF EXISTS `user_0`;
CREATE TABLE `user_0` (
                        `id` int(10) NOT NULL COMMENT '用户id',
                        `user_name` varchar(50) NOT NULL COMMENT '用户名',
                        `user_age` int(3) DEFAULT '0' COMMENT '用户年龄',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='db_0 用户表';

DROP TABLE IF EXISTS `user_1`;
CREATE TABLE `user_1` (
                        `id` int(10) NOT NULL COMMENT '用户id',
                        `user_name` varchar(50) NOT NULL COMMENT '用户名',
                        `user_age` int(3) DEFAULT '0' COMMENT '用户年龄',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='db_0 用户表';

DROP TABLE IF EXISTS `user_0`;
CREATE TABLE `user_0` (
                        `id` int(10) NOT NULL COMMENT '用户id',
                        `user_name` varchar(50) NOT NULL COMMENT '用户名',
                        `user_age` int(3) DEFAULT '0' COMMENT '用户年龄',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='db_1 用户表';

DROP TABLE IF EXISTS `user_1`;
CREATE TABLE `user_1` (
                        `id` int(10) NOT NULL COMMENT '用户id',
                        `user_name` varchar(50) NOT NULL COMMENT '用户名',
                        `user_age` int(3) DEFAULT '0' COMMENT '用户年龄',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='db_1 用户表';