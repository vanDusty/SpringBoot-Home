DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
                         `id` BIGINT ( 20 ) AUTO_INCREMENT PRIMARY KEY COMMENT "自增主键",
                         `user_name` VARCHAR ( 50 ) NOT NULL COMMENT "用户名",
                         `user_age` INT ( 3 ) DEFAULT 0 COMMENT "用户年龄"
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT "测试用户表";

-- 插入测试数据

INSERT INTO `tb_user` VALUES (1, "张三", 27);
INSERT INTO `tb_user` VALUES (2, "李四", 30);
INSERT INTO `tb_user` VALUES (3, "王五", 20);