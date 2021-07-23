/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : arknights

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2021-04-13 14:17:30
*/
DROP DATABASE IF EXISTS `arknights`;
CREATE DATABASE `arknights`;
USE `arknights`;

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for a_admin_user
-- ----------------------------
DROP TABLE IF EXISTS `a_admin_user`;
CREATE TABLE `a_admin_user`  (
  `qq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限狗列表',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `found` int(0) NULL DEFAULT 0 COMMENT '无限抽卡权限',
  `img` int(0) NULL DEFAULT 0 COMMENT '无限涩图权限',
  `six` int(0) NULL DEFAULT 0 COMMENT '概率拉满权限',
  `sql` int(0) NULL DEFAULT 0 COMMENT 'sql执行权限',
  `upload` int(0) NULL DEFAULT 1 COMMENT '涩图管理权限',
  PRIMARY KEY (`qq`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for a_agent
-- ----------------------------
DROP TABLE IF EXISTS `a_agent`;
CREATE TABLE `a_agent`  (
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `star` int(0) NULL DEFAULT NULL,
  `pool` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `limit` int(0) NULL DEFAULT 0 COMMENT '0->非限定\r\n1->周年限定\r\n2->联动限定\r\n3->五倍权值\r\n4->新年限定',
  PRIMARY KEY (`name`, `pool`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for a_bili_dynamic
-- ----------------------------
DROP TABLE IF EXISTS `a_bili_dynamic`;
CREATE TABLE `a_bili_dynamic`  (
  `uid` int(0) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `top` bigint(0) NULL DEFAULT 0,
  `first` bigint(0) NULL DEFAULT 0,
  `second` bigint(0) NULL DEFAULT 0,
  `third` bigint(0) NULL DEFAULT 0,
  `fourth` bigint(0) NULL DEFAULT 0,
  `fifth` bigint(0) NULL DEFAULT 0,
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for a_data_version
-- ----------------------------
DROP TABLE IF EXISTS `a_data_version`;
CREATE TABLE `a_data_version`  (
  `data_version` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`data_version`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for a_group_admin
-- ----------------------------
DROP TABLE IF EXISTS `a_group_admin`;
CREATE TABLE `a_group_admin`  (
  `group_id` int(0) NOT NULL,
  `found` int(0) NULL DEFAULT 20,
  `picture` int(0) NULL DEFAULT 5,
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for a_image_url
-- ----------------------------
DROP TABLE IF EXISTS `a_image_url`;
CREATE TABLE `a_image_url`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `url` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `type` int(0) NULL DEFAULT NULL COMMENT '0:表情包/1:涩图',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for a_model_count
-- ----------------------------
DROP TABLE IF EXISTS `a_model_count`;
CREATE TABLE `a_model_count`  (
  `model_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `count` bigint(0) NULL DEFAULT 0,
  PRIMARY KEY (`model_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for a_nick_name
-- ----------------------------
DROP TABLE IF EXISTS `a_nick_name`;
CREATE TABLE `a_nick_name`  (
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`nick_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for a_user_found
-- ----------------------------
DROP TABLE IF EXISTS `a_user_found`;
CREATE TABLE `a_user_found`  (
  `qq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `found_count` int(0) NULL DEFAULT 0 COMMENT '垫刀数',
  `today_count` int(0) NULL DEFAULT 0 COMMENT '今日累计抽卡数',
  `all_count` int(0) NULL DEFAULT 0,
  `all_six` int(0) NULL DEFAULT 0,
  `today_six` int(0) NULL DEFAULT 0,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `group_id` bigint(0) NULL DEFAULT NULL,
  `today_five` int(0) NULL DEFAULT 0,
  `pixiv` int(0) NULL DEFAULT 0,
  `search` int(0) NULL DEFAULT 0 COMMENT '十连寻访次数',
  PRIMARY KEY (`qq`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_enemy
-- ----------------------------
DROP TABLE IF EXISTS `t_enemy`;
CREATE TABLE `t_enemy`  (
  `enemy_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `atk` int(0) NULL DEFAULT 0,
  `baseAttackTime` double NULL DEFAULT 0,
  `def` int(0) NULL DEFAULT 0,
  `hpRecoveryPerSec` int(0) NULL DEFAULT 0,
  `magicResistance` int(0) NULL DEFAULT 0,
  `massLevel` int(0) NULL DEFAULT 0,
  `maxHp` int(0) NULL DEFAULT 0,
  `moveSpeed` double(255, 1) NULL DEFAULT 0.0,
  `rangeRadius` double(255, 1) NULL DEFAULT 0.0,
  `level` int(0) NOT NULL DEFAULT 0,
  `silenceImmune` int(0) NULL DEFAULT 0 COMMENT '沉默免疫',
  `sleepImmune` int(0) NULL DEFAULT 0 COMMENT '睡眠免疫',
  `stunImmune` int(0) NULL DEFAULT 0 COMMENT '眩晕免疫',
  PRIMARY KEY (`enemy_id`, `level`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_material
-- ----------------------------
DROP TABLE IF EXISTS `t_material`;
CREATE TABLE `t_material`  (
  `material_id` int(0) NOT NULL,
  `material_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `material_icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `material_pic` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`material_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_material_made
-- ----------------------------
DROP TABLE IF EXISTS `t_material_made`;
CREATE TABLE `t_material_made`  (
  `made_id` int(0) NOT NULL AUTO_INCREMENT,
  `material_id` int(0) NULL DEFAULT NULL,
  `use_material_id` int(0) NULL DEFAULT NULL,
  `use_number` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`made_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_matrix
-- ----------------------------
DROP TABLE IF EXISTS `t_matrix`;
CREATE TABLE `t_matrix`  (
  `matrix_id` int(0) NOT NULL AUTO_INCREMENT,
  `stage_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `item_id` int(0) NULL DEFAULT NULL,
  `quantity` int(0) NULL DEFAULT NULL,
  `times` int(0) NULL DEFAULT NULL,
  `rate` double(255, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`matrix_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_operator
-- ----------------------------
DROP TABLE IF EXISTS `t_operator`;
CREATE TABLE `t_operator`  (
  `operator_id` int(0) NOT NULL AUTO_INCREMENT,
  `char_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '官方id',
  `operator_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `operator_rarity` tinyint(0) NULL DEFAULT NULL,
  `operator_class` tinyint(0) NULL DEFAULT NULL COMMENT '先锋: 1,近卫: 2,重装: 3,狙击: 4,术师: 5,辅助: 6,医疗: 7,特种: 8',
  `available` tinyint(0) NULL DEFAULT 0,
  `in_limit` tinyint(0) NULL DEFAULT NULL,
  `atk` int(0) NULL DEFAULT 0 COMMENT '攻击',
  `def` int(0) NULL DEFAULT 0 COMMENT '防御',
  `magicResistance` int(0) NULL DEFAULT 0 COMMENT '魔抗',
  `maxHp` int(0) NULL DEFAULT 0 COMMENT '最大生命',
  `blockCnt` int(0) NULL DEFAULT 0 COMMENT '阻挡数',
  `cost` int(0) NULL DEFAULT 0 COMMENT '费用消耗',
  `baseAttackTime` double NULL DEFAULT 0 COMMENT '攻击间隔',
  `respawnTime` int(0) NULL DEFAULT 0 COMMENT '再部署时间',
  `draw_name` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '画师',
  `info_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '声优',
  `code_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代号',
  `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `come_from` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出身地',
  `birthday` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `race` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '种族',
  `height` int(0) NULL DEFAULT NULL COMMENT '身高',
  `infection` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '感染情况',
  `comprehensive_test` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '综合体检测试',
  `objective_resume` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '客观履历',
  `clinical_diagnosis` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '临床诊断分析',
  `archives1` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `archives2` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `archives3` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `archives4` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `promotion_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '晋升资料',
  PRIMARY KEY (`operator_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_operator_building_skill
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_building_skill`;
CREATE TABLE `t_operator_building_skill`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `operator_id` int(0) NULL DEFAULT NULL,
  `phase` int(0) NULL DEFAULT NULL,
  `level` int(0) NULL DEFAULT NULL,
  `buff_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `room_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_operator_evolve_costs
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_evolve_costs`;
CREATE TABLE `t_operator_evolve_costs`  (
  `cost_id` int(0) NOT NULL AUTO_INCREMENT,
  `operator_id` int(0) NULL DEFAULT NULL,
  `evolve_level` tinyint(0) NULL DEFAULT NULL,
  `use_material_id` int(0) NULL DEFAULT NULL,
  `use_number` tinyint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`cost_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_operator_png
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_png`;
CREATE TABLE `t_operator_png`  (
  `char_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `char_base` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`char_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_operator_skill
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_skill`;
CREATE TABLE `t_operator_skill`  (
  `skill_id` int(0) NOT NULL AUTO_INCREMENT,
  `operator_id` int(0) NULL DEFAULT NULL,
  `skill_index` tinyint(0) NULL DEFAULT NULL,
  `skill_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`skill_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_operator_skill_desc
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_skill_desc`;
CREATE TABLE `t_operator_skill_desc`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `skill_id` int(0) NULL DEFAULT NULL,
  `skill_type` int(0) NULL DEFAULT NULL COMMENT '0->被动,1->手动触发,2->自动触发',
  `sp_type` int(0) NULL DEFAULT NULL COMMENT '1->自动回复,2->攻击回复,3->,4->受击回复,8->被动?',
  `sp_cost` int(0) NULL DEFAULT NULL COMMENT '技力消耗',
  `sp_init` int(0) NULL DEFAULT NULL COMMENT '初始技力',
  `duration` int(0) NULL DEFAULT NULL COMMENT '持续时间',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '技能描述',
  `skill_level` int(0) NULL DEFAULT NULL COMMENT '技能等级',
  `max_charge` int(0) NULL DEFAULT NULL COMMENT '最大充能数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_operator_skill_mastery_costs
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_skill_mastery_costs`;
CREATE TABLE `t_operator_skill_mastery_costs`  (
  `cost_id` int(0) NOT NULL AUTO_INCREMENT,
  `skill_id` int(0) NULL DEFAULT NULL,
  `mastery_level` tinyint(0) NULL DEFAULT NULL,
  `use_material_id` int(0) NULL DEFAULT NULL,
  `use_number` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`cost_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_operator_skin
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_skin`;
CREATE TABLE `t_operator_skin`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `operator_id` int(0) NULL DEFAULT NULL COMMENT '干员id',
  `skin_group_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `skin_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '皮肤代号',
  `skin_base64` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片base64编码',
  `dialog` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `drawer_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '画师名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_operator_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_tags_relation`;
CREATE TABLE `t_operator_tags_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `operator_rarity` tinyint(0) NOT NULL,
  `operator_tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_operator_talent
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_talent`;
CREATE TABLE `t_operator_talent`  (
  `talent_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '天赋id',
  `operator_id` int(0) NULL DEFAULT NULL COMMENT '干员id',
  `talent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '天赋名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '天赋描述',
  `phase` int(0) NULL DEFAULT NULL COMMENT '解锁精英化',
  `level` int(0) NULL DEFAULT NULL COMMENT '解锁等级',
  `potential` int(0) NULL DEFAULT NULL COMMENT '解锁潜能',
  PRIMARY KEY (`talent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_stage
-- ----------------------------
DROP TABLE IF EXISTS `t_stage`;
CREATE TABLE `t_stage`  (
  `stage_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `zone_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ap_cost` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`stage_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_zone
-- ----------------------------
DROP TABLE IF EXISTS `t_zone`;
CREATE TABLE `t_zone`  (
  `zone_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `zone_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`zone_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
