/*
 Navicat Premium Data Transfer

 Source Server         : q'q
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44-log)
 Source Host           : localhost:3306
 Source Schema         : tongcheng

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44-log)
 File Encoding         : 65001

 Date: 06/06/2025 12:04:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (2, 'qqqqqq', 'qqqqqqqqqqq', '2025-06-05 17:06:01');
INSERT INTO `users` VALUES (3, 'taoli', '11', '2025-06-05 17:08:21');
INSERT INTO `users` VALUES (6, 'qqqqq', 'qqqq', '2025-06-05 17:38:17');
INSERT INTO `users` VALUES (8, 'atp', '111', '2025-06-05 17:40:50');
INSERT INTO `users` VALUES (9, 'ATPr', '123', '2025-06-05 18:14:53');

SET FOREIGN_KEY_CHECKS = 1;
