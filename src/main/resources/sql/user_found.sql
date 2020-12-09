/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : arknights

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-12-08 17:28:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user_found
-- ----------------------------
DROP TABLE IF EXISTS `user_found`;
CREATE TABLE `user_found` (
  `qq` bigint(255) NOT NULL,
  `found_count` int(255) DEFAULT NULL COMMENT '垫刀数',
  `today_count` int(255) DEFAULT NULL COMMENT '今日累计抽卡数',
  PRIMARY KEY (`qq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
