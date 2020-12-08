/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : arknights

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-12-08 10:37:04
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

-- ----------------------------
-- Records of agent
-- ----------------------------
INSERT INTO `agent` VALUES ('克洛丝', '3', '常规');
INSERT INTO `agent` VALUES ('守林人', '5', '锁与钥的守护者');
INSERT INTO `agent` VALUES ('斯卡蒂', '6', '常规');
INSERT INTO `agent` VALUES ('梅', '4', '锁与钥的守护者');
INSERT INTO `agent` VALUES ('槐琥', '5', '锁与钥的守护者');
INSERT INTO `agent` VALUES ('狮蝎', '5', '常规');
INSERT INTO `agent` VALUES ('玫兰莎', '3', '常规');
INSERT INTO `agent` VALUES ('莫斯提马', '6', '常规');
INSERT INTO `agent` VALUES ('莫斯提马', '6', '锁与钥的守护者');
INSERT INTO `agent` VALUES ('蓝毒', '5', '常规');
INSERT INTO `agent` VALUES ('蛇屠箱', '4', '常规');
INSERT INTO `agent` VALUES ('远山', '4', '常规');
INSERT INTO `agent` VALUES ('银灰', '6', '常规');
INSERT INTO `agent` VALUES ('陈', '6', '常规');
INSERT INTO `agent` VALUES ('黑', '6', '常规');
