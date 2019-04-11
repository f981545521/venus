CREATE TABLE `boss` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL DEFAULT '',
  `age` int(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='老板';

-- ----------------------------
-- Records of t_boss
-- ----------------------------
INSERT INTO `boss` VALUES ('1', '找饭店', '23');
INSERT INTO `boss` VALUES ('2', '查出吧v', '23');
INSERT INTO `boss` VALUES ('3', '吃必须不把', '23');
INSERT INTO `boss` VALUES ('4', '小明,校长', '20');
