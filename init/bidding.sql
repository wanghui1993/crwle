/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50722
Source Host           : localhost:3306
Source Database       : bidding

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2020-11-30 15:59:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ccgp
-- ----------------------------
DROP TABLE IF EXISTS `ccgp`;
CREATE TABLE `ccgp` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(175) NOT NULL COMMENT '标题',
  `href` varchar(175) DEFAULT NULL COMMENT '超链接地址',
  `time` varchar(175) DEFAULT NULL COMMENT '时间',
  `key_word` varchar(175) DEFAULT NULL COMMENT '关键字',
  `bid_type` int(5) DEFAULT NULL,
  `content` text,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`title`)
) ENGINE=InnoDB AUTO_INCREMENT=4227 DEFAULT CHARSET=utf8mb4 COMMENT='政府采购网';

-- ----------------------------
-- Table structure for ccgp_copy
-- ----------------------------
DROP TABLE IF EXISTS `ccgp_copy`;
CREATE TABLE `ccgp_copy` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(175) NOT NULL COMMENT '标题',
  `href` text COMMENT '超链接地址',
  `time` varchar(175) DEFAULT NULL COMMENT '时间',
  `key_word` varchar(175) DEFAULT NULL COMMENT '关键字',
  `bid_type` int(5) DEFAULT NULL,
  `content` mediumtext,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for ggzy_bz
-- ----------------------------
DROP TABLE IF EXISTS `ggzy_bz`;
CREATE TABLE `ggzy_bz` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(175) NOT NULL COMMENT '标题',
  `href` text COMMENT '超链接地址',
  `time` varchar(175) DEFAULT NULL COMMENT '时间',
  `key_word` varchar(175) DEFAULT NULL COMMENT '关键字',
  `bid_type` int(5) DEFAULT NULL,
  `content` mediumtext,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`title`)
) ENGINE=InnoDB AUTO_INCREMENT=173 DEFAULT CHARSET=utf8mb4 COMMENT='滨州公共资源交易中心';

-- ----------------------------
-- Table structure for ggzy_dz
-- ----------------------------
DROP TABLE IF EXISTS `ggzy_dz`;
CREATE TABLE `ggzy_dz` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(175) NOT NULL COMMENT '标题',
  `href` text COMMENT '超链接地址',
  `time` varchar(175) DEFAULT NULL COMMENT '时间',
  `key_word` varchar(175) DEFAULT NULL COMMENT '关键字',
  `bid_type` int(5) DEFAULT NULL,
  `content` mediumtext,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`title`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COMMENT='德州公共资源交易中心';

-- ----------------------------
-- Table structure for ggzy_hz
-- ----------------------------
DROP TABLE IF EXISTS `ggzy_hz`;
CREATE TABLE `ggzy_hz` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(175) NOT NULL COMMENT '标题',
  `href` text COMMENT '超链接地址',
  `time` varchar(175) DEFAULT NULL COMMENT '时间',
  `key_word` varchar(175) DEFAULT NULL COMMENT '关键字',
  `bid_type` int(5) DEFAULT NULL,
  `content` mediumtext,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菏泽公共资源交易中心';

-- ----------------------------
-- Table structure for ggzy_jn
-- ----------------------------
DROP TABLE IF EXISTS `ggzy_jn`;
CREATE TABLE `ggzy_jn` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(175) NOT NULL COMMENT '标题',
  `href` text COMMENT '超链接地址',
  `time` varchar(175) DEFAULT NULL COMMENT '时间',
  `key_word` varchar(175) DEFAULT NULL COMMENT '关键字',
  `bid_type` int(5) DEFAULT NULL,
  `content` mediumtext,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`title`)
) ENGINE=InnoDB AUTO_INCREMENT=1342 DEFAULT CHARSET=utf8mb4 COMMENT='济南公共资源交易中心';

-- ----------------------------
-- Table structure for ggzy_lc
-- ----------------------------
DROP TABLE IF EXISTS `ggzy_lc`;
CREATE TABLE `ggzy_lc` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(175) NOT NULL COMMENT '标题',
  `href` text COMMENT '超链接地址',
  `time` varchar(175) DEFAULT NULL COMMENT '时间',
  `key_word` varchar(175) DEFAULT NULL COMMENT '关键字',
  `bid_type` int(5) DEFAULT NULL,
  `content` mediumtext,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`title`)
) ENGINE=InnoDB AUTO_INCREMENT=330 DEFAULT CHARSET=utf8mb4 COMMENT='聊城公共资源交易中心';

-- ----------------------------
-- Table structure for ggzy_ly
-- ----------------------------
DROP TABLE IF EXISTS `ggzy_ly`;
CREATE TABLE `ggzy_ly` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(175) NOT NULL COMMENT '标题',
  `href` text COMMENT '超链接地址',
  `time` varchar(175) DEFAULT NULL COMMENT '时间',
  `key_word` varchar(175) DEFAULT NULL COMMENT '关键字',
  `bid_type` int(5) DEFAULT NULL,
  `content` mediumtext,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`title`)
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=utf8mb4 COMMENT='临沂公共资源交易中心';

-- ----------------------------
-- Table structure for ggzy_qd
-- ----------------------------
DROP TABLE IF EXISTS `ggzy_qd`;
CREATE TABLE `ggzy_qd` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(175) NOT NULL COMMENT '标题',
  `href` text COMMENT '超链接地址',
  `time` varchar(175) DEFAULT NULL COMMENT '时间',
  `key_word` varchar(175) DEFAULT NULL COMMENT '关键字',
  `bid_type` int(5) DEFAULT NULL,
  `content` text,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`title`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='青岛公共资源交易中心';

-- ----------------------------
-- Table structure for ggzy_rz
-- ----------------------------
DROP TABLE IF EXISTS `ggzy_rz`;
CREATE TABLE `ggzy_rz` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(175) NOT NULL COMMENT '标题',
  `href` text COMMENT '超链接地址',
  `time` varchar(175) DEFAULT NULL COMMENT '时间',
  `key_word` varchar(175) DEFAULT NULL COMMENT '关键字',
  `bid_type` int(5) DEFAULT NULL,
  `content` mediumtext,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`title`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8mb4 COMMENT='日照公共资源交易中心';

-- ----------------------------
-- Table structure for ggzy_wf
-- ----------------------------
DROP TABLE IF EXISTS `ggzy_wf`;
CREATE TABLE `ggzy_wf` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(175) NOT NULL COMMENT '标题',
  `href` text COMMENT '超链接地址',
  `time` varchar(175) DEFAULT NULL COMMENT '时间',
  `key_word` varchar(175) DEFAULT NULL COMMENT '关键字',
  `bid_type` int(5) DEFAULT NULL,
  `content` mediumtext,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`title`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COMMENT='潍坊公共资源交易中心';

-- ----------------------------
-- Table structure for ggzy_wh
-- ----------------------------
DROP TABLE IF EXISTS `ggzy_wh`;
CREATE TABLE `ggzy_wh` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(175) NOT NULL COMMENT '标题',
  `href` text COMMENT '超链接地址',
  `time` varchar(175) DEFAULT NULL COMMENT '时间',
  `key_word` varchar(175) DEFAULT NULL COMMENT '关键字',
  `bid_type` int(5) DEFAULT NULL,
  `content` mediumtext,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`title`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='威海公共资源交易中心';

-- ----------------------------
-- Table structure for ggzy_yt
-- ----------------------------
DROP TABLE IF EXISTS `ggzy_yt`;
CREATE TABLE `ggzy_yt` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(175) NOT NULL COMMENT '标题',
  `href` text COMMENT '超链接地址',
  `time` varchar(175) DEFAULT NULL COMMENT '时间',
  `key_word` varchar(175) DEFAULT NULL COMMENT '关键字',
  `bid_type` int(5) DEFAULT NULL,
  `content` mediumtext,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`title`)
) ENGINE=InnoDB AUTO_INCREMENT=233 DEFAULT CHARSET=utf8mb4 COMMENT='烟台公共资源交易中心';

-- ----------------------------
-- Table structure for ggzy_zb
-- ----------------------------
DROP TABLE IF EXISTS `ggzy_zb`;
CREATE TABLE `ggzy_zb` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(175) NOT NULL COMMENT '标题',
  `href` text COMMENT '超链接地址',
  `time` varchar(175) DEFAULT NULL COMMENT '时间',
  `key_word` varchar(175) DEFAULT NULL COMMENT '关键字',
  `bid_type` int(5) DEFAULT NULL,
  `content` mediumtext,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`,`title`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COMMENT='淄博公共资源交易中心';
