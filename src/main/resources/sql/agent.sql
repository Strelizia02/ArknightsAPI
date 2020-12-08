/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : arknights

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-12-08 17:28:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for agent
-- ----------------------------
DROP TABLE IF EXISTS `agent`;
CREATE TABLE `agent` (
  `name` varchar(255) NOT NULL,
  `star` int(20) DEFAULT NULL,
  `pool` varchar(255) NOT NULL,
  PRIMARY KEY (`name`,`pool`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
