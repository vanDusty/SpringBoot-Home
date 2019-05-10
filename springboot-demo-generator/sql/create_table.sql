DROP TABLE IF EXISTS `tbl_user`;
CREATE TABLE `tbl_user` (
  `id` bigint(20) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '主键id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名称',
  `user_age` int(11) DEFAULT NULL COMMENT '用户年龄'
);