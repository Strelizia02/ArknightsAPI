-- ----------------------------
-- Table structure for a_admin_user
-- ----------------------------
DROP TABLE IF EXISTS `a_admin_user`;
CREATE TABLE `a_admin_user`  (
  `qq` varchar(255) NOT NULL,
  `name` varchar(255) NULL DEFAULT NULL,
  `found` int(0) NULL DEFAULT 0,
  `img` int(0) NULL DEFAULT 0,
  `six` int(0) NULL DEFAULT 0,
  `sql` int(0) NULL DEFAULT 0,
  `upload` int(0) NULL DEFAULT 1,
  PRIMARY KEY (`qq`)
);


-- ----------------------------
-- Table structure for a_agent
-- ----------------------------
DROP TABLE IF EXISTS `a_agent`;
CREATE TABLE `a_agent`  (
  `name` varchar(255)NOT NULL,
  `star` int(0) NULL DEFAULT NULL,
  `pool` varchar(255) NOT NULL,
  `limit` int(0) NULL DEFAULT 0 ,
  PRIMARY KEY (`name`, `pool`)
);

-- ----------------------------
-- Table structure for a_bili_dynamic
-- ----------------------------
DROP TABLE IF EXISTS `a_bili_dynamic`;
CREATE TABLE `a_bili_dynamic`  (
  `uid` bigint(0) NOT NULL,
  `name` varchar(255) NULL DEFAULT NULL,
  `top` bigint(0) NULL DEFAULT 0,
  `first` bigint(0) NULL DEFAULT 0,
  `second` bigint(0) NULL DEFAULT 0,
  `third` bigint(0) NULL DEFAULT 0,
  `fourth` bigint(0) NULL DEFAULT 0,
  `fifth` bigint(0) NULL DEFAULT 0,
  PRIMARY KEY (`uid`)
);

-- ----------------------------
-- Table structure for a_group_bili_rel
-- ----------------------------
DROP TABLE IF EXISTS `a_group_bili_rel`;
CREATE TABLE `a_group_bili_rel`  (
  `group_id` bigint(0) NOT NULL,
  `uid` bigint(0) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for a_data_version
-- ----------------------------
DROP TABLE IF EXISTS `a_data_version`;
CREATE TABLE `a_data_version`  (
  `data_version` varchar(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`data_version`)
);

-- ----------------------------
-- Table structure for a_group_admin
-- ----------------------------
DROP TABLE IF EXISTS `a_group_admin`;
CREATE TABLE `a_group_admin`  (
  `group_id` int(0) NOT NULL,
  `found` int(0) NULL DEFAULT 20,
  `picture` int(0) NULL DEFAULT 0,
  PRIMARY KEY (`group_id`)
);

-- ----------------------------
-- Table structure for a_image_url
-- ----------------------------
DROP TABLE IF EXISTS `a_image_url`;
CREATE TABLE `a_image_url`  (
  `id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `url` longtext NULL,
  `type` int(0) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for a_model_count
-- ----------------------------
DROP TABLE IF EXISTS `a_model_count`;
CREATE TABLE `a_model_count`  (
  `model_name` varchar(255) NOT NULL,
  `count` bigint(0) NULL DEFAULT 0,
  PRIMARY KEY (`model_name`)
);

-- ----------------------------
-- Table structure for a_nick_name
-- ----------------------------
DROP TABLE IF EXISTS `a_nick_name`;
CREATE TABLE `a_nick_name`  (
  `nick_name` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`nick_name`)
);

-- ----------------------------
-- Table structure for a_question
-- ----------------------------
DROP TABLE IF EXISTS `a_question`;
CREATE TABLE `a_question`  (
  `id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `description` varchar(255) NULL DEFAULT NULL,
  `answer` varchar(255) DEFAULT NULL,
  `attr` int(0) NULL DEFAULT NULL,
  `type` int(0) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for a_user_found
-- ----------------------------
DROP TABLE IF EXISTS `a_user_found`;
CREATE TABLE `a_user_found`  (
  `qq` varchar(255) NOT NULL,
  `found_count` int(0) NULL DEFAULT 0,
  `today_count` int(0) NULL DEFAULT 0,
  `all_count` int(0) NULL DEFAULT 0,
  `all_six` int(0) NULL DEFAULT 0,
  `today_six` int(0) NULL DEFAULT 0,
  `name` varchar(255)  NULL DEFAULT NULL,
  `group_id` bigint(0) NULL DEFAULT NULL,
  `today_five` int(0) NULL DEFAULT 0,
  `pixiv` int(0) NULL DEFAULT 0,
  `search` int(0) NULL DEFAULT 0,
  PRIMARY KEY (`qq`)
);

-- ----------------------------
-- Table structure for t_enemy
-- ----------------------------
DROP TABLE IF EXISTS `t_enemy`;
CREATE TABLE `t_enemy`  (
  `enemy_id` varchar(255) NOT NULL,
  `name` varchar(255) NULL DEFAULT NULL,
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
  `silenceImmune` int(0) NULL DEFAULT 0,
  `sleepImmune` int(0) NULL DEFAULT 0,
  `stunImmune` int(0) NULL DEFAULT 0,
  PRIMARY KEY (`enemy_id`, `level`)
);

-- ----------------------------
-- Table structure for t_equip
-- ----------------------------
DROP TABLE IF EXISTS `t_equip`;
CREATE TABLE `t_equip`  (
  `id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `equip_id` varchar(255) NOT NULL,
  `equip_name` varchar(255) NOT NULL,
  `char_id` varchar(255) NULL DEFAULT NULL,
  `phase` int(0) NULL DEFAULT NULL,
  `level` int(0) NULL DEFAULT NULL,
  `desc` varchar(255) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for t_equip_buff
-- ----------------------------
DROP TABLE IF EXISTS `t_equip_buff`;
CREATE TABLE `t_equip_buff`  (
  `buff_id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `equip_id` varchar(255) NULL DEFAULT NULL,
  `buff_name` varchar(255) NULL DEFAULT NULL,
  `value` double(255, 2) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for t_equip_cost
-- ----------------------------
DROP TABLE IF EXISTS `t_equip_cost`;
CREATE TABLE `t_equip_cost`  (
  `id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `equip_id` varchar(255) NULL DEFAULT NULL,
  `material_id` varchar(255) NULL DEFAULT NULL,
  `use_number` int(0) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for t_equip_mission
-- ----------------------------
DROP TABLE IF EXISTS `t_equip_mission`;
CREATE TABLE `t_equip_mission`  (
  `id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `equip_id` varchar(255) NULL DEFAULT NULL,
  `mission_id` varchar(255) NULL DEFAULT NULL,
  `mission_desc` varchar(255) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for t_material
-- ----------------------------
DROP TABLE IF EXISTS `t_material`;
CREATE TABLE `t_material`  (
  `material_id` varchar(255) NOT NULL,
  `material_name` varchar(255) NULL DEFAULT NULL,
  `material_icon` varchar(255) NULL DEFAULT NULL,
  `material_pic` longtext NULL,
  PRIMARY KEY (`material_id`)
);

-- ----------------------------
-- Table structure for t_material_made
-- ----------------------------
DROP TABLE IF EXISTS `t_material_made`;
CREATE TABLE `t_material_made`  (
  `made_id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `material_id` varchar(255) NULL DEFAULT NULL,
  `use_material_id` varchar(255) NULL DEFAULT NULL,
  `use_number` int(0) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for t_matrix
-- ----------------------------
DROP TABLE IF EXISTS `t_matrix`;
CREATE TABLE `t_matrix`  (
  `matrix_id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `stage_id` varchar(255) NULL DEFAULT NULL,
  `item_id` varchar(255) NULL DEFAULT NULL,
  `quantity` int(0) NULL DEFAULT NULL,
  `times` int(0) NULL DEFAULT NULL,
  `rate` double(255, 2) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for t_operator
-- ----------------------------
DROP TABLE IF EXISTS `t_operator`;
CREATE TABLE `t_operator`  (
  `operator_id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `char_id` varchar(255) NULL DEFAULT NULL,
  `operator_name` varchar(255) NULL DEFAULT NULL,
  `operator_rarity` tinyint(0) NULL DEFAULT NULL,
  `operator_class` tinyint(0) NULL DEFAULT NULL,
  `available` tinyint(0) NULL DEFAULT 0,
  `in_limit` tinyint(0) NULL DEFAULT NULL,
  `atk` int(0) NULL DEFAULT 0,
  `def` int(0) NULL DEFAULT 0,
  `magicResistance` int(0) NULL DEFAULT 0,
  `maxHp` int(0) NULL DEFAULT 0,
  `blockCnt` int(0) NULL DEFAULT 0,
  `cost` int(0) NULL DEFAULT 0,
  `baseAttackTime` double NULL DEFAULT 0 ,
  `respawnTime` int(0) NULL DEFAULT 0,
  `draw_name` text NULL,
  `info_name` varchar(255) NULL DEFAULT NULL,
  `code_name` varchar(255) NULL DEFAULT NULL,
  `sex` varchar(255) NULL DEFAULT NULL,
  `come_from` varchar(255) NULL DEFAULT NULL,
  `birthday` varchar(255) NULL DEFAULT NULL,
  `race` varchar(255) NULL DEFAULT NULL,
  `height` int(0) NULL DEFAULT NULL,
  `infection` varchar(255) NULL DEFAULT NULL,
  `comprehensive_test` text NULL,
  `objective_resume` text NULL,
  `clinical_diagnosis` text NULL,
  `archives1` text NULL,
  `archives2` text NULL,
  `archives3` text NULL,
  `archives4` text NULL,
  `promotion_info` text NULL
);

-- ----------------------------
-- Table structure for t_operator_building_skill
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_building_skill`;
CREATE TABLE `t_operator_building_skill`  (
  `id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `operator_id` int(0) NULL DEFAULT NULL,
  `phase` int(0) NULL DEFAULT NULL,
  `level` int(0) NULL DEFAULT NULL,
  `buff_name` varchar(255) NULL DEFAULT NULL,
  `room_type` varchar(255) NULL DEFAULT NULL,
  `description` varchar(255) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for t_operator_evolve_costs
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_evolve_costs`;
CREATE TABLE `t_operator_evolve_costs`  (
  `cost_id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `operator_id` int(0) NULL DEFAULT NULL,
  `evolve_level` tinyint(0) NULL DEFAULT NULL,
  `use_material_id` varchar(255) NULL DEFAULT NULL,
  `use_number` tinyint(0) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for t_operator_png
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_png`;
CREATE TABLE `t_operator_png`  (
  `char_id` varchar(255) NOT NULL,
  `char_base` longtext NULL,
  PRIMARY KEY (`char_id`)
);

-- ----------------------------
-- Table structure for t_operator_skill
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_skill`;
CREATE TABLE `t_operator_skill`  (
  `skill_id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `operator_id` int(0) NULL DEFAULT NULL,
  `skill_index` tinyint(0) NULL DEFAULT NULL,
  `skill_name` varchar(255) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for t_operator_skill_desc
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_skill_desc`;
CREATE TABLE `t_operator_skill_desc`  (
  `id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `skill_id` int(0) NULL DEFAULT NULL,
  `skill_type` int(0) NULL DEFAULT NULL,
  `sp_type` int(0) NULL DEFAULT NULL,
  `sp_cost` int(0) NULL DEFAULT NULL,
  `sp_init` int(0) NULL DEFAULT NULL,
  `duration` int(0) NULL DEFAULT NULL,
  `description` varchar(255) NULL DEFAULT NULL,
  `skill_level` int(0) NULL DEFAULT NULL,
  `max_charge` int(0) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for t_operator_skill_mastery_costs
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_skill_mastery_costs`;
CREATE TABLE `t_operator_skill_mastery_costs`  (
  `cost_id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `skill_id` int(0) NULL DEFAULT NULL,
  `mastery_level` tinyint(0) NULL DEFAULT NULL,
  `use_material_id` varchar(255) NULL DEFAULT NULL,
  `use_number` int(0) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for t_operator_skin
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_skin`;
CREATE TABLE `t_operator_skin`  (
  `id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `operator_id` varchar(255) NULL DEFAULT NULL,
  `skin_group_name` varchar(255) NULL DEFAULT NULL,
  `skin_name` varchar(255) NULL DEFAULT NULL,
  `skin_base64` longtext NULL,
  `dialog` varchar(255) NULL DEFAULT NULL,
  `drawer_name` varchar(255) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for t_operator_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_tags_relation`;
CREATE TABLE `t_operator_tags_relation`  (
  `id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `operator_name` varchar(255) NOT NULL,
  `operator_rarity` tinyint(0) NOT NULL,
  `operator_tags` varchar(255) NOT NULL
);

-- ----------------------------
-- Table structure for t_operator_talent
-- ----------------------------
DROP TABLE IF EXISTS `t_operator_talent`;
CREATE TABLE `t_operator_talent`  (
  `talent_id` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `operator_id` int(0) NULL DEFAULT NULL,
  `talent_name` varchar(255) NULL DEFAULT NULL,
  `description` varchar(255) NULL DEFAULT NULL,
  `phase` int(0) NULL DEFAULT NULL,
  `level` int(0) NULL DEFAULT NULL,
  `potential` int(0) NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for t_stage
-- ----------------------------
DROP TABLE IF EXISTS `t_stage`;
CREATE TABLE `t_stage`  (
  `stage_id` varchar(255) NOT NULL,
  `zone_id` varchar(255) NULL DEFAULT NULL,
  `code` varchar(255) NULL DEFAULT NULL,
  `ap_cost` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`stage_id`)
);

-- ----------------------------
-- Table structure for t_zone
-- ----------------------------
DROP TABLE IF EXISTS `t_zone`;
CREATE TABLE `t_zone`  (
  `zone_id` varchar(255) NOT NULL,
  `zone_name` varchar(255) NULL DEFAULT NULL,
  PRIMARY KEY (`zone_id`)
);


-- ----------------------------
-- Table structure for a_login_user
-- ----------------------------
DROP TABLE IF EXISTS `a_login_user`;
CREATE TABLE `a_login_user`  (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NULL DEFAULT NULL,
  `token` varchar(255) NULL DEFAULT NULL,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`username`)
);

-- ----------------------------
-- Table structure for a_activity
-- ----------------------------
DROP TABLE IF EXISTS `a_activity`;
CREATE TABLE `a_activity`  (
  `type` int(255) NOT NULL,
  `time` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`time`)
);
