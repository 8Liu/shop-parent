/*
Navicat MySQL Data Transfer

Source Server         : 刘德煌
Source Server Version : 50722
Source Host           : 127.0.0.1:3306
Source Database       : shop-pay

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-02-27 16:11:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for payment_info
-- ----------------------------
DROP TABLE IF EXISTS `payment_info`;
CREATE TABLE `payment_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL COMMENT '客户id',
  `typeid` int(2) DEFAULT NULL COMMENT '支付类型',
  `orderid` varchar(50) DEFAULT NULL COMMENT '订单id',
  `price` decimal(10,0) DEFAULT NULL COMMENT '价格',
  `source` varchar(10) DEFAULT NULL COMMENT '支付信息来源',
  `state` int(2) DEFAULT NULL COMMENT '支付状态',
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `platformorderid` varchar(100) DEFAULT NULL COMMENT '支付宝第三方ID',
  `payMessage` varchar(10000) DEFAULT NULL COMMENT '支付信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;
