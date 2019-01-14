/*
Navicat MySQL Data Transfer

Source Server         : 58.2.221.69 for mysql
Source Server Version : 50615
Source Host           : 58.2.221.69:3306
Source Database       : a710009498

Target Server Type    : MYSQL
Target Server Version : 50615
File Encoding         : 65001

Date: 2019-01-14 16:27:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `LEAVE_BILL`
-- ----------------------------
DROP TABLE IF EXISTS `LEAVE_BILL`;
CREATE TABLE `LEAVE_BILL` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `leave_days` int(11) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `delete_flag` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_022113d9fe64444881774fb7d11` (`user_id`),
  CONSTRAINT `FK_022113d9fe64444881774fb7d11` FOREIGN KEY (`user_id`) REFERENCES `SYS_USERS` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of LEAVE_BILL
-- ----------------------------
INSERT INTO `LEAVE_BILL` VALUES ('21', '2', '年假申请', '没啥好说的', '0', '2', '0');
INSERT INTO `LEAVE_BILL` VALUES ('23', '3', 'reson by json update', 'remark by json update', '0', '2', '0');

-- ----------------------------
-- Table structure for `SYS_PERMISSION`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_PERMISSION`;
CREATE TABLE `SYS_PERMISSION` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `url` varchar(200) NOT NULL,
  `pid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of SYS_PERMISSION
-- ----------------------------
INSERT INTO `SYS_PERMISSION` VALUES ('1', 'ROLE_USER', '请假列表', '/leavebill/list', null);
INSERT INTO `SYS_PERMISSION` VALUES ('2', 'ROLE_ADMIN', '部署流程', '/flowable/process/deployment', null);
INSERT INTO `SYS_PERMISSION` VALUES ('3', 'ROLE_ADMIN', '请假流程', '/leavebill/list', null);

-- ----------------------------
-- Table structure for `SYS_PERMISSION_ROLE`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_PERMISSION_ROLE`;
CREATE TABLE `SYS_PERMISSION_ROLE` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) unsigned NOT NULL,
  `permission_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of SYS_PERMISSION_ROLE
-- ----------------------------
INSERT INTO `SYS_PERMISSION_ROLE` VALUES ('1', '1', '1');
INSERT INTO `SYS_PERMISSION_ROLE` VALUES ('2', '1', '2');
INSERT INTO `SYS_PERMISSION_ROLE` VALUES ('3', '2', '1');
INSERT INTO `SYS_PERMISSION_ROLE` VALUES ('4', '2', '3');

-- ----------------------------
-- Table structure for `SYS_ROLE`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE`;
CREATE TABLE `SYS_ROLE` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of SYS_ROLE
-- ----------------------------
INSERT INTO `SYS_ROLE` VALUES ('1', 'ROLE_ADMIN');
INSERT INTO `SYS_ROLE` VALUES ('2', 'ROLE_USER');

-- ----------------------------
-- Table structure for `SYS_ROLE_USER`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE_USER`;
CREATE TABLE `SYS_ROLE_USER` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `sys_user_id` bigint(20) unsigned NOT NULL,
  `sys_role_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of SYS_ROLE_USER
-- ----------------------------
INSERT INTO `SYS_ROLE_USER` VALUES ('1', '1', '1');
INSERT INTO `SYS_ROLE_USER` VALUES ('2', '2', '2');

-- ----------------------------
-- Table structure for `SYS_USERS`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_USERS`;
CREATE TABLE `SYS_USERS` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `manager_id` bigint(20) DEFAULT NULL,
  `delete_flag` int(1) DEFAULT NULL,
  `hr_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_37bbbf636a464177898e9c9bc3e` (`manager_id`),
  CONSTRAINT `FK_37bbbf636a464177898e9c9bc3e` FOREIGN KEY (`manager_id`) REFERENCES `SYS_USERS` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of SYS_USERS
-- ----------------------------
INSERT INTO `SYS_USERS` VALUES ('1', 'admin', 'admin', '2', '0', '5');
INSERT INTO `SYS_USERS` VALUES ('2', 'sxia', 'admin', '2', '0', '5');
INSERT INTO `SYS_USERS` VALUES ('3', 'manager', 'admin', '2', '0', '5');
INSERT INTO `SYS_USERS` VALUES ('4', 'dev', 'admin', '2', '0', '5');
INSERT INTO `SYS_USERS` VALUES ('5', 'hr', 'admin', '2', '0', '5');
