CREATE DATABASE IF NOT EXISTS arknights DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
USE arknights;
-- 干员-卡池表
CREATE TABLE `a_agent` (
  `name` varchar(255) NOT NULL,
  `star` int(20) DEFAULT NULL,
  `pool` varchar(255) NOT NULL,
  PRIMARY KEY (`name`,`pool`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- 群友抽卡记录表
CREATE TABLE `a_user_found` (
  `qq` varchar(255) NOT NULL,
  `found_count` int(255) DEFAULT NULL COMMENT '垫刀数',
  `today_count` int(255) DEFAULT NULL COMMENT '今日累计抽卡数',
  `all_count` int(255) DEFAULT '0',
  `all_six` int(255) DEFAULT '0',
  `today_six` int(255) DEFAULT '0',
  `name` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `group_id` bigint(255) DEFAULT NULL,
  `today_five` int(255) DEFAULT '0',
  PRIMARY KEY (`qq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- 材料表
CREATE TABLE `t_material` (
  `material_id` int(11) NOT NULL,
  `material_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `material_nickname` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`material_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
-- 材料合成表
CREATE TABLE `t_material_made` (
  `made_id` int(11) NOT NULL AUTO_INCREMENT,
  `material_id` int(11) DEFAULT NULL,
  `use_material_id` int(11) DEFAULT NULL,
  `use_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`made_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
-- 材料获取关卡表
CREATE TABLE `t_material_source` (
  `source_id` int(11) NOT NULL AUTO_INCREMENT,
  `material_id` int(11) DEFAULT NULL,
  `source_place` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `source_rate` tinyint(4) DEFAULT NULL COMMENT '罕见: 1,小概率: 2,中概率: 3,大概率: 4,固定: 5',
  PRIMARY KEY (`source_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
-- 干员表
CREATE TABLE `t_operator` (
  `operator_id` int(11) NOT NULL AUTO_INCREMENT,
  `operator_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `operator_rarity` tinyint(4) DEFAULT NULL,
  `operator_class` tinyint(4) DEFAULT NULL COMMENT '先锋: 1,近卫: 2,重装: 3,狙击: 4,术师: 5,辅助: 6,医疗: 7,特种: 8',
  `available` tinyint(4) DEFAULT '0',
  `in_limit` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`operator_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=178 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
-- 干员精英化材料表
CREATE TABLE `t_operator_evolve_costs` (
  `cost_id` int(11) NOT NULL AUTO_INCREMENT,
  `operator_id` int(11) DEFAULT NULL,
  `evolve_level` tinyint(4) DEFAULT NULL,
  `use_material_id` int(11) DEFAULT NULL,
  `use_number` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`cost_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=865 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
-- 干员技能表
CREATE TABLE `t_operator_skill` (
  `skill_id` int(11) NOT NULL AUTO_INCREMENT,
  `operator_id` int(11) DEFAULT NULL,
  `skill_index` tinyint(4) DEFAULT NULL,
  `skill_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`skill_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=343 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
-- 技能专精材料表
CREATE TABLE `t_operator_skill_mastery_costs` (
  `cost_id` int(11) NOT NULL AUTO_INCREMENT,
  `skill_id` int(11) DEFAULT NULL,
  `mastery_level` tinyint(4) DEFAULT NULL,
  `use_material_id` int(11) DEFAULT NULL,
  `use_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`cost_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2890 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
-- 干员Tag表
CREATE TABLE `t_operator_tags_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operator_name` varchar(255) NOT NULL,
  `operator_rarity` tinyint(4) NOT NULL,
  `operator_tags` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb4;