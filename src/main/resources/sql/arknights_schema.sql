/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : arknights

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2021-01-12 09:40:11
*/
DROP DATABASE IF EXISTS `arknights`;
CREATE DATABASE `arknights`;
USE `arknights`;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for a_admin_user
-- ----------------------------
DROP TABLE IF EXISTS `a_admin_user`;
CREATE TABLE `a_admin_user` (
  `qq` varchar(255) NOT NULL COMMENT '权限狗列表',
  `name` varchar(255) DEFAULT NULL,
  `found` int(2) DEFAULT '0' COMMENT '无限抽卡权限',
  `img` int(2) DEFAULT '0' COMMENT '无限涩图权限',
  `six` int(2) DEFAULT '0' COMMENT '概率拉满权限',
  `sql` int(2) DEFAULT '0' COMMENT 'sql执行权限',
  PRIMARY KEY (`qq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for a_agent
-- ----------------------------
DROP TABLE IF EXISTS `a_agent`;
CREATE TABLE `a_agent` (
  `name` varchar(255) NOT NULL,
  `star` int(20) DEFAULT NULL,
  `pool` varchar(255) NOT NULL,
  `limit` int(2) DEFAULT '0',
  PRIMARY KEY (`name`,`pool`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for a_bili_dynamic
-- ----------------------------
DROP TABLE IF EXISTS `a_bili_dynamic`;
CREATE TABLE `a_bili_dynamic` (
  `uid` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `top` bigint(255) DEFAULT '0',
  `first` bigint(255) DEFAULT '0',
  `second` bigint(255) DEFAULT '0',
  `third` bigint(255) DEFAULT '0',
  `fourth` bigint(255) DEFAULT '0',
  `fifth` bigint(255) DEFAULT '0',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for a_data_version
-- ----------------------------
DROP TABLE IF EXISTS `a_data_version`;
CREATE TABLE `a_data_version` (
  `data_version` varchar(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`data_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for a_image_url
-- ----------------------------
DROP TABLE IF EXISTS `a_image_url`;
CREATE TABLE `a_image_url` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `url` longtext,
  `type` int(10) DEFAULT NULL COMMENT '0:表情包/1:涩图',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for a_user_found
-- ----------------------------
DROP TABLE IF EXISTS `a_user_found`;
CREATE TABLE `a_user_found` (
  `qq` varchar(255) NOT NULL,
  `found_count` int(255) DEFAULT '0' COMMENT '垫刀数',
  `today_count` int(255) DEFAULT '0' COMMENT '今日累计抽卡数',
  `all_count` int(255) DEFAULT '0',
  `all_six` int(255) DEFAULT '0',
  `today_six` int(255) DEFAULT '0',
  `name` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `group_id` bigint(255) DEFAULT NULL,
  `today_five` int(255) DEFAULT '0',
  `pixiv` int(255) DEFAULT '0',
  PRIMARY KEY (`qq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_enemy
-- ----------------------------
DROP TABLE IF EXISTS `t_enemy`;
CREATE TABLE `t_enemy` (
  `enemy_id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `atk` int(255) DEFAULT '0',
  `baseAttackTime` int(255) DEFAULT '0',
  `def` int(255) DEFAULT '0',
  `hpRecoveryPerSec` int(255) DEFAULT '0',
  `magicResistance` int(255) DEFAULT '0',
  `massLevel` int(255) DEFAULT '0',
  `maxHp` int(255) DEFAULT '0',
  `moveSpeed` double(255,1) DEFAULT '0.0',
  `rangeRadius` double(255,1) DEFAULT '0.0',
  `level` int(255) NOT NULL DEFAULT '0',
  `silenceImmune` int(255) DEFAULT '0' COMMENT '沉默免疫',
  `sleepImmune` int(255) DEFAULT '0' COMMENT '睡眠免疫',
  `stunImmune` int(255) DEFAULT '0' COMMENT '眩晕免疫',
  PRIMARY KEY (`enemy_id`,`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_material
-- ----------------------------
DROP TABLE IF EXISTS `t_material`;
CREATE TABLE `t_material` (
  `material_id` int(11) NOT NULL,
  `material_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_material_made
-- ----------------------------
DROP TABLE IF EXISTS `t_material_made`;
CREATE TABLE `t_material_made` (
  `made_id` int(11) NOT NULL AUTO_INCREMENT,
  `material_id` int(11) DEFAULT NULL,
  `use_material_id` int(11) DEFAULT NULL,
  `use_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`made_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_matrix
-- ----------------------------
DROP TABLE IF EXISTS `t_matrix`;
CREATE TABLE `t_matrix` (
  `matrix_id` int(255) NOT NULL AUTO_INCREMENT,
  `stage_id` varchar(255) DEFAULT NULL,
  `item_id` int(255) DEFAULT NULL,
  `quantity` int(255) DEFAULT NULL,
  `times` int(255) DEFAULT NULL,
  `rate` double(255,2) DEFAULT NULL,
  PRIMARY KEY (`matrix_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_operator
-- ----------------------------
DROP TABLE IF EXISTS `t_operator`;
CREATE TABLE `t_operator` (
  `operator_id` int(11) NOT NULL AUTO_INCREMENT,
  `operator_name` varchar(255) DEFAULT NULL,
  `operator_rarity` tinyint(4) DEFAULT NULL,
  `operator_class` tinyint(4) DEFAULT NULL COMMENT '先锋: 1,近卫: 2,重装: 3,狙击: 4,术师: 5,辅助: 6,医疗: 7,特种: 8',
  `available` tinyint(4) DEFAULT '0',
  `in_limit` tinyint(4) DEFAULT NULL,
  `atk` int(11) DEFAULT '0' COMMENT '攻击',
  `def` int(11) DEFAULT '0' COMMENT '防御',
  `magicResistance` int(11) DEFAULT '0' COMMENT '魔抗',
  `maxHp` int(11) DEFAULT '0' COMMENT '最大生命',
  `blockCnt` int(11) DEFAULT '0' COMMENT '阻挡数',
  `cost` int(11) DEFAULT '0' COMMENT '费用消耗',
  `baseAttackTime` int(11) DEFAULT '0' COMMENT '攻击间隔',
  `respawnTime` int(11) DEFAULT '0' COMMENT '再部署时间',
  PRIMARY KEY (`operator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_operator_evolve_costs
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_evolve_costs`;
CREATE TABLE `t_operator_evolve_costs` (
  `cost_id` int(11) NOT NULL AUTO_INCREMENT,
  `operator_id` int(11) DEFAULT NULL,
  `evolve_level` tinyint(4) DEFAULT NULL,
  `use_material_id` int(11) DEFAULT NULL,
  `use_number` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`cost_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_operator_skill
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_skill`;
CREATE TABLE `t_operator_skill` (
  `skill_id` int(11) NOT NULL AUTO_INCREMENT,
  `operator_id` int(11) DEFAULT NULL,
  `skill_index` tinyint(4) DEFAULT NULL,
  `skill_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`skill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_operator_skill_mastery_costs
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_skill_mastery_costs`;
CREATE TABLE `t_operator_skill_mastery_costs` (
  `cost_id` int(11) NOT NULL AUTO_INCREMENT,
  `skill_id` int(11) DEFAULT NULL,
  `mastery_level` tinyint(4) DEFAULT NULL,
  `use_material_id` int(11) DEFAULT NULL,
  `use_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`cost_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_operator_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_tags_relation`;
CREATE TABLE `t_operator_tags_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operator_name` varchar(255) NOT NULL,
  `operator_rarity` tinyint(4) NOT NULL,
  `operator_tags` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_stage
-- ----------------------------
DROP TABLE IF EXISTS `t_stage`;
CREATE TABLE `t_stage` (
  `stage_id` varchar(255) NOT NULL,
  `zone_id` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `ap_cost` int(255) DEFAULT NULL,
  PRIMARY KEY (`stage_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_zone
-- ----------------------------
DROP TABLE IF EXISTS `t_zone`;
CREATE TABLE `t_zone` (
  `zone_id` varchar(255) NOT NULL,
  `zone_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`zone_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
