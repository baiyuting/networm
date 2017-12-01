/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-11-30 07:50:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for word_count
-- ----------------------------
DROP TABLE IF EXISTS `word_count`;
CREATE TABLE `word_count` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `word` varchar(255) NOT NULL,
  `count` int(11) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `uk_word_time` (`word`,`create_time`) USING BTREE COMMENT '单词和时间处理',
  KEY `idx_count` (`count`) COMMENT '词频索引',
  KEY `idx_create_time` (`create_time`) USING BTREE COMMENT '创建时间添加索引方便查当天数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
