/*
Navicat MySQL Data Transfer

Source Server         : 刘德煌
Source Server Version : 50722
Source Host           : 127.0.0.1:3306
Source Database       : order-info

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-02-27 16:13:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orderNumber` varchar(255) DEFAULT NULL COMMENT '订单编号',
  `isPay` int(50) DEFAULT NULL COMMENT '0 未支付，1已支付',
  `payId` varchar(100) DEFAULT NULL,
  `userId` int(50) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
