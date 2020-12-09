/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : arknights

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-12-09 10:32:55
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
INSERT INTO `agent` VALUES ('12F', '2', '公招');
INSERT INTO `agent` VALUES ('Castle-3', '1', '公招');
INSERT INTO `agent` VALUES ('Lanset-2', '1', '公招');
INSERT INTO `agent` VALUES ('THRM-EX', '1', '公招');
INSERT INTO `agent` VALUES ('W', '6', '常规');
INSERT INTO `agent` VALUES ('临光', '5', '常规');
INSERT INTO `agent` VALUES ('亚叶', '5', '活动');
INSERT INTO `agent` VALUES ('伊桑', '4', '凭证兑换');
INSERT INTO `agent` VALUES ('伊芙利特', '6', '常规');
INSERT INTO `agent` VALUES ('傀影', '6', '常规');
INSERT INTO `agent` VALUES ('克洛丝', '3', '常规');
INSERT INTO `agent` VALUES ('凛冬', '5', '常规');
INSERT INTO `agent` VALUES ('初雪', '5', '常规');
INSERT INTO `agent` VALUES ('刻俄柏', '6', '常规');
INSERT INTO `agent` VALUES ('刻刀', '4', '公招');
INSERT INTO `agent` VALUES ('华法琳', '5', '常规');
INSERT INTO `agent` VALUES ('卡缇', '3', '常规');
INSERT INTO `agent` VALUES ('卡达', '4', '常规');
INSERT INTO `agent` VALUES ('古米', '4', '常规');
INSERT INTO `agent` VALUES ('可颂', '5', '常规');
INSERT INTO `agent` VALUES ('史尔特尔', '6', '常规');
INSERT INTO `agent` VALUES ('史都华德', '3', '常规');
INSERT INTO `agent` VALUES ('吽', '5', '常规');
INSERT INTO `agent` VALUES ('嘉维尔', '4', '凭证兑换');
INSERT INTO `agent` VALUES ('四月', '5', '常规');
INSERT INTO `agent` VALUES ('因陀罗', '5', '公招');
INSERT INTO `agent` VALUES ('地灵', '4', '常规');
INSERT INTO `agent` VALUES ('坚雷', '4', '凭证兑换');
INSERT INTO `agent` VALUES ('塞雷娅', '6', '常规');
INSERT INTO `agent` VALUES ('夜刀', '2', '公招');
INSERT INTO `agent` VALUES ('夜烟', '4', '常规');
INSERT INTO `agent` VALUES ('夜莺', '6', '常规');
INSERT INTO `agent` VALUES ('夜魔', '5', '常规');
INSERT INTO `agent` VALUES ('天火', '5', '常规');
INSERT INTO `agent` VALUES ('奥斯塔', '5', '常规');
INSERT INTO `agent` VALUES ('孑', '4', '常规');
INSERT INTO `agent` VALUES ('守林人', '5', '常规');
INSERT INTO `agent` VALUES ('守林人', '5', '锁与钥的守卫者');
INSERT INTO `agent` VALUES ('安哲拉', '5', '常规');
INSERT INTO `agent` VALUES ('安德切尔', '3', '常规');
INSERT INTO `agent` VALUES ('安比尔', '4', '常规');
INSERT INTO `agent` VALUES ('安洁莉娜', '6', '常规');
INSERT INTO `agent` VALUES ('安赛尔', '3', '常规');
INSERT INTO `agent` VALUES ('宴', '4', '常规');
INSERT INTO `agent` VALUES ('崖心', '5', '常规');
INSERT INTO `agent` VALUES ('巡林者', '2', '公招');
INSERT INTO `agent` VALUES ('巫恋', '5', '常规');
INSERT INTO `agent` VALUES ('布洛卡', '5', '常规');
INSERT INTO `agent` VALUES ('年', '6', '常规');
INSERT INTO `agent` VALUES ('幽灵鲨', '5', '常规');
INSERT INTO `agent` VALUES ('微风', '5', '凭证兑换');
INSERT INTO `agent` VALUES ('德克萨斯', '5', '常规');
INSERT INTO `agent` VALUES ('惊蛰', '5', '常规');
INSERT INTO `agent` VALUES ('慑砂', '5', '常规');
INSERT INTO `agent` VALUES ('慕斯', '4', '常规');
INSERT INTO `agent` VALUES ('拉普兰德', '5', '常规');
INSERT INTO `agent` VALUES ('拜松', '5', '活动');
INSERT INTO `agent` VALUES ('推进之王', '6', '常规');
INSERT INTO `agent` VALUES ('斑点', '3', '常规');
INSERT INTO `agent` VALUES ('断崖', '5', '常规');
INSERT INTO `agent` VALUES ('断罪者', '4', '活动');
INSERT INTO `agent` VALUES ('斯卡蒂', '6', '常规');
INSERT INTO `agent` VALUES ('早露', '6', '常规');
INSERT INTO `agent` VALUES ('星极', '5', '常规');
INSERT INTO `agent` VALUES ('星熊', '6', '常规');
INSERT INTO `agent` VALUES ('普罗旺斯', '5', '常规');
INSERT INTO `agent` VALUES ('暗索', '4', '常规');
INSERT INTO `agent` VALUES ('暴行', '5', '活动');
INSERT INTO `agent` VALUES ('月禾', '5', '常规');
INSERT INTO `agent` VALUES ('月见夜', '3', '常规');
INSERT INTO `agent` VALUES ('末药', '4', '常规');
INSERT INTO `agent` VALUES ('杜宾', '4', '常规');
INSERT INTO `agent` VALUES ('杜林', '2', '公招');
INSERT INTO `agent` VALUES ('杰克', '4', '常规');
INSERT INTO `agent` VALUES ('杰西卡', '4', '常规');
INSERT INTO `agent` VALUES ('极境', '5', '常规');
INSERT INTO `agent` VALUES ('柏喙', '5', '活动');
INSERT INTO `agent` VALUES ('格劳克斯', '5', '常规');
INSERT INTO `agent` VALUES ('格拉尼', '5', '活动');
INSERT INTO `agent` VALUES ('格雷伊', '4', '常规');
INSERT INTO `agent` VALUES ('桃金娘', '4', '常规');
INSERT INTO `agent` VALUES ('梅', '4', '常规');
INSERT INTO `agent` VALUES ('梅', '4', '锁与钥的守卫者');
INSERT INTO `agent` VALUES ('梅尔', '5', '常规');
INSERT INTO `agent` VALUES ('梓兰', '3', '常规');
INSERT INTO `agent` VALUES ('棘刺', '6', '常规');
INSERT INTO `agent` VALUES ('森蚺', '6', '常规');
INSERT INTO `agent` VALUES ('槐琥', '5', '常规');
INSERT INTO `agent` VALUES ('槐琥', '5', '锁与钥的守卫者');
INSERT INTO `agent` VALUES ('泡普卡', '3', '常规');
INSERT INTO `agent` VALUES ('泡泡', '4', '常规');
INSERT INTO `agent` VALUES ('波登可', '4', '常规');
INSERT INTO `agent` VALUES ('泥岩', '6', '常规');
INSERT INTO `agent` VALUES ('流星', '4', '常规');
INSERT INTO `agent` VALUES ('深海色', '4', '常规');
INSERT INTO `agent` VALUES ('清流', '4', '活动');
INSERT INTO `agent` VALUES ('清道夫', '4', '常规');
INSERT INTO `agent` VALUES ('温蒂', '6', '常规');
INSERT INTO `agent` VALUES ('火神', '5', '公招');
INSERT INTO `agent` VALUES ('灰喉', '5', '常规');
INSERT INTO `agent` VALUES ('炎客', '5', '活动');
INSERT INTO `agent` VALUES ('炎熔', '3', '常规');
INSERT INTO `agent` VALUES ('煌', '6', '常规');
INSERT INTO `agent` VALUES ('燧石', '5', '常规');
INSERT INTO `agent` VALUES ('特米米', '5', '活动');
INSERT INTO `agent` VALUES ('狮蝎', '5', '常规');
INSERT INTO `agent` VALUES ('猎蜂', '4', '常规');
INSERT INTO `agent` VALUES ('玫兰莎', '3', '常规');
INSERT INTO `agent` VALUES ('瑕光', '6', '常规');
INSERT INTO `agent` VALUES ('白金', '5', '常规');
INSERT INTO `agent` VALUES ('白雪', '4', '常规');
INSERT INTO `agent` VALUES ('白面鸮', '5', '常规');
INSERT INTO `agent` VALUES ('真理', '5', '常规');
INSERT INTO `agent` VALUES ('石棉', '5', '常规');
INSERT INTO `agent` VALUES ('砾', '4', '常规');
INSERT INTO `agent` VALUES ('稀音', '5', '活动');
INSERT INTO `agent` VALUES ('空', '5', '常规');
INSERT INTO `agent` VALUES ('空爆', '3', '常规');
INSERT INTO `agent` VALUES ('米格鲁', '3', '常规');
INSERT INTO `agent` VALUES ('絮雨', '5', '常规');
INSERT INTO `agent` VALUES ('红', '5', '常规');
INSERT INTO `agent` VALUES ('红云', '4', '常规');
INSERT INTO `agent` VALUES ('红豆', '4', '常规');
INSERT INTO `agent` VALUES ('缠丸', '4', '常规');
INSERT INTO `agent` VALUES ('翎羽', '3', '常规');
INSERT INTO `agent` VALUES ('能天使', '6', '常规');
INSERT INTO `agent` VALUES ('艾斯黛尔', '4', '公招');
INSERT INTO `agent` VALUES ('艾雅法拉', '6', '常规');
INSERT INTO `agent` VALUES ('芙兰卡', '5', '常规');
INSERT INTO `agent` VALUES ('芙蓉', '3', '常规');
INSERT INTO `agent` VALUES ('芬', '3', '常规');
INSERT INTO `agent` VALUES ('芳汀', '4', '常规');
INSERT INTO `agent` VALUES ('苇草', '5', '常规');
INSERT INTO `agent` VALUES ('苏苏洛', '4', '常规');
INSERT INTO `agent` VALUES ('苦艾', '5', '活动');
INSERT INTO `agent` VALUES ('莫斯提马', '6', '常规');
INSERT INTO `agent` VALUES ('莫斯提马', '6', '锁与钥的守卫者');
INSERT INTO `agent` VALUES ('莱恩哈特', '5', '常规');
INSERT INTO `agent` VALUES ('蓝毒', '5', '常规');
INSERT INTO `agent` VALUES ('薄绿', '5', '活动');
INSERT INTO `agent` VALUES ('蛇屠箱', '4', '常规');
INSERT INTO `agent` VALUES ('蜜蜡', '5', '常规');
INSERT INTO `agent` VALUES ('角峰', '4', '常规');
INSERT INTO `agent` VALUES ('讯使', '4', '凭证兑换');
INSERT INTO `agent` VALUES ('诗怀雅', '5', '常规');
INSERT INTO `agent` VALUES ('调香师', '4', '常规');
INSERT INTO `agent` VALUES ('贾维', '5', '常规');
INSERT INTO `agent` VALUES ('赫拉格', '6', '常规');
INSERT INTO `agent` VALUES ('赫默', '5', '常规');
INSERT INTO `agent` VALUES ('远山', '4', '常规');
INSERT INTO `agent` VALUES ('迷迭香', '6', '常规');
INSERT INTO `agent` VALUES ('送葬人', '5', '常规');
INSERT INTO `agent` VALUES ('酸糖', '4', '常规');
INSERT INTO `agent` VALUES ('铃兰', '6', '常规');
INSERT INTO `agent` VALUES ('银灰', '6', '常规');
INSERT INTO `agent` VALUES ('铸铁', '5', '活动');
INSERT INTO `agent` VALUES ('锡兰', '5', '活动');
INSERT INTO `agent` VALUES ('闪灵', '6', '常规');
INSERT INTO `agent` VALUES ('阿', '6', '常规');
INSERT INTO `agent` VALUES ('阿消', '4', '常规');
INSERT INTO `agent` VALUES ('阿米娅', '5', '初始');
INSERT INTO `agent` VALUES ('陈', '6', '常规');
INSERT INTO `agent` VALUES ('陨星', '5', '常规');
INSERT INTO `agent` VALUES ('雪雉', '5', '活动');
INSERT INTO `agent` VALUES ('雷蛇', '5', '常规');
INSERT INTO `agent` VALUES ('霜叶', '4', '常规');
INSERT INTO `agent` VALUES ('鞭刃', '5', '活动');
INSERT INTO `agent` VALUES ('风笛', '6', '常规');
INSERT INTO `agent` VALUES ('食铁兽', '5', '常规');
INSERT INTO `agent` VALUES ('香草', '3', '常规');
INSERT INTO `agent` VALUES ('麦哲伦', '6', '常规');
INSERT INTO `agent` VALUES ('黑', '6', '常规');
INSERT INTO `agent` VALUES ('黑角', '2', '公招');
