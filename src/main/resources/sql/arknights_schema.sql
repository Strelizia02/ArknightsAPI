CREATE DATABASE IF NOT EXISTS arknights DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
USE arknights;
CREATE TABLE `agent` (
  `name` varchar(255) NOT NULL,
  `star` int(20) DEFAULT NULL,
  `pool` varchar(255) NOT NULL,
  PRIMARY KEY (`name`,`pool`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_found` (
  `qq` varchar(255) NOT NULL,
  `found_count` int(255) DEFAULT NULL COMMENT '垫刀数',
  `today_count` int(255) DEFAULT NULL COMMENT '今日累计抽卡数',
  PRIMARY KEY (`qq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


