CREATE TABLE `family_reward_record` (
                                      `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                      `family_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '商品名称',
                                      `reward_type` int(10) NOT NULL DEFAULT '1' COMMENT '商品库存数量',
                                      `state` int(1) NOT NULL DEFAULT '0' COMMENT '商品状态',
                                      `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
                                      `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=270 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家庭领取奖励表（家庭内多人只能有一个人能领取成功，不能重复领取）';

CREATE TABLE `good` (
                      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                      `good_name` varchar(255) NOT NULL COMMENT '商品名称',
                      `good_counts` int(255) NOT NULL COMMENT '商品库存',
                      `create_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
                      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

INSERT INTO `good` VALUES (1, '哇哈哈', 5, '2019-09-20 17:39:04');
INSERT INTO `good` VALUES (2, '卫龙', 5, '2019-09-20 17:39:06');