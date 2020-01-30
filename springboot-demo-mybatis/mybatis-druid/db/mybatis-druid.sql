DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`
(
  `id`        bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '' 自增主键 '',
  `user_name` varchar(50) NOT NULL COMMENT '' 用户名 '',
  `user_age`  int(3) DEFAULT 0 COMMENT '' 用户年龄 ''
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- 插入测试数据

INSERT INTO `user_info`
VALUES (1, '' 张三 '', 27);
INSERT INTO `user_info`
VALUES (2, '' 李四 '', 30);
INSERT INTO `user_info`
VALUES (3, '' 王五 '', 20);