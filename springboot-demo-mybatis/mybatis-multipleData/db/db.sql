-- 主库用户表（master）

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                      `id` bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
                      `user_name` varchar(50) NOT NULL COMMENT '用户名',
                      `user_age` int(3) DEFAULT 0 COMMENT '用户年龄'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '主库用户表';

INSERT INTO `user` VALUES (1, '张三', 27);

-- 从库slave1用户表

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                      `id` bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
                      `user_name` varchar(50) NOT NULL COMMENT '用户名',
                      `user_age` int(3) DEFAULT 0 COMMENT '用户年龄'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '从库slave1用户表';

INSERT INTO `user` VALUES (2, '李四', 30);


-- 从库slave2用户表

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                      `id` bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
                      `user_name` varchar(50) NOT NULL COMMENT '用户名',
                      `user_age` int(3) DEFAULT 0 COMMENT '用户年龄'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '从库slave2用户表';

INSERT INTO `user` VALUES (3, '王五', 20);