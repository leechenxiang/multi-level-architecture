/*
 Navicat Premium Data Transfer

 Source Server         : MySql in Docker - 192.168.1.60
 Source Server Type    : MySQL
 Source Server Version : 80033
 Source Host           : 192.168.1.60:3306
 Source Schema         : my-shop

 Target Server Type    : MySQL
 Target Server Version : 80033
 File Encoding         : 65001

 Date: 06/07/2023 11:57:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for item_category
-- ----------------------------
DROP TABLE IF EXISTS `item_category`;
CREATE TABLE `item_category` (
  `id` int NOT NULL COMMENT '主键id',
  `category_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品分类名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of item_category
-- ----------------------------
BEGIN;
INSERT INTO `item_category` VALUES (1001, '玩具');
INSERT INTO `item_category` VALUES (1002, '母婴');
INSERT INTO `item_category` VALUES (1003, '生活用品');
INSERT INTO `item_category` VALUES (1004, '音像制品');
INSERT INTO `item_category` VALUES (2001, '书籍');
INSERT INTO `item_category` VALUES (2002, '服饰');
INSERT INTO `item_category` VALUES (3001, '手机');
INSERT INTO `item_category` VALUES (3002, '电脑');
INSERT INTO `item_category` VALUES (3003, '数码产品');
INSERT INTO `item_category` VALUES (4001, '家具用品');
INSERT INTO `item_category` VALUES (4002, '食品');
INSERT INTO `item_category` VALUES (4003, '线材');
INSERT INTO `item_category` VALUES (5001, '软件服务');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
