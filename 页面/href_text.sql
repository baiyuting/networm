/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-11-30 07:51:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for href_text
-- ----------------------------
DROP TABLE IF EXISTS `href_text`;
CREATE TABLE `href_text` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `text` varchar(255) NOT NULL COMMENT 'a标签中的文本信息',
  `flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否已经分析过',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_flag` (`flag`) COMMENT '是否爬过标志位'
) ENGINE=InnoDB AUTO_INCREMENT=2143 DEFAULT CHARSET=utf8;
