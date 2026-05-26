/*
 Navicat Premium Data Transfer

 Source Server         : qq
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44-log)
 Source Host           : localhost:3306
 Source Schema         : javaweb_staffsystem

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44-log)
 File Encoding         : 65001

 Date: 24/12/2025 21:29:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attendance
-- ----------------------------
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance`  (
  `attendance_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `check_date` date NULL DEFAULT NULL,
  `check_in_time` time NULL DEFAULT NULL,
  `check_out_time` time NULL DEFAULT NULL,
  `status` enum('正常','迟到','早退','缺勤','请假','加班') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '正常',
  `work_hours` decimal(6, 2) NULL DEFAULT 0.00,
  `overtime_hours` decimal(6, 2) NULL DEFAULT 0.00,
  PRIMARY KEY (`attendance_id`) USING BTREE,
  UNIQUE INDEX `uk_attendance_user_date`(`user_id`, `check_date`) USING BTREE,
  CONSTRAINT `fk_attendance_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attendance
-- ----------------------------
INSERT INTO `attendance` VALUES (1, 1, '2025-12-07', '15:39:00', '15:40:00', '正常', 0.02, 0.00);
INSERT INTO `attendance` VALUES (5, 1, '2025-12-14', '15:24:00', '15:24:00', '正常', 0.00, 0.00);
INSERT INTO `attendance` VALUES (6, 1, '2025-12-18', '15:36:00', '15:36:00', '迟到', 0.00, 0.00);
INSERT INTO `attendance` VALUES (18, 14, '2025-12-26', '23:10:00', '23:10:00', '正常', 0.00, 0.00);
INSERT INTO `attendance` VALUES (19, 15, '2025-12-11', '23:35:00', '23:36:00', '正常', 0.02, 0.00);

-- ----------------------------
-- Table structure for departmentmenbers
-- ----------------------------
DROP TABLE IF EXISTS `departmentmenbers`;
CREATE TABLE `departmentmenbers`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `department_id` int(11) NOT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `join_date` datetime NULL DEFAULT NULL,
  `role_in_department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_department_user`(`department_id`, `user_id`) USING BTREE,
  INDEX `fk_departmentmenbers_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `fk_departmentmenbers_department_id` FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_departmentmenbers_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of departmentmenbers
-- ----------------------------
INSERT INTO `departmentmenbers` VALUES (2, 1, 3, NULL, NULL);
INSERT INTO `departmentmenbers` VALUES (3, 1, 2, NULL, NULL);

-- ----------------------------
-- Table structure for departments
-- ----------------------------
DROP TABLE IF EXISTS `departments`;
CREATE TABLE `departments`  (
  `department_id` int(11) NOT NULL AUTO_INCREMENT,
  `department_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `manager_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`department_id`) USING BTREE,
  INDEX `fk_departments_manager_id`(`manager_id`) USING BTREE,
  CONSTRAINT `fk_departments_manager_id` FOREIGN KEY (`manager_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of departments
-- ----------------------------
INSERT INTO `departments` VALUES (1, '技术部', 3);
INSERT INTO `departments` VALUES (3, '技术部', 1);

-- ----------------------------
-- Table structure for financial_records
-- ----------------------------
DROP TABLE IF EXISTS `financial_records`;
CREATE TABLE `financial_records`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT,
  `record_type` enum('收入','支出','投资','薪资','其他') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '其他',
  `amount` decimal(12, 2) NOT NULL DEFAULT 0.00,
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `record_date` date NOT NULL,
  `created_by` int(11) NULL DEFAULT NULL COMMENT '记录人（用户ID）',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `related_project_id` int(11) NULL DEFAULT NULL COMMENT '关联项目（可选）',
  `category` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '财务类别，如：办公支出、研发费用、销售收入等',
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `fk_financial_created_by`(`created_by`) USING BTREE,
  INDEX `fk_financial_project_id`(`related_project_id`) USING BTREE,
  CONSTRAINT `fk_financial_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_financial_project_id` FOREIGN KEY (`related_project_id`) REFERENCES `projects` (`project_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of financial_records
-- ----------------------------

-- ----------------------------
-- Table structure for projectmenbers
-- ----------------------------
DROP TABLE IF EXISTS `projectmenbers`;
CREATE TABLE `projectmenbers`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `role_in_project` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `join_date` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_project_user`(`project_id`, `user_id`) USING BTREE,
  INDEX `fk_projectmenbers_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `fk_projectmenbers_project_id` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_projectmenbers_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of projectmenbers
-- ----------------------------
INSERT INTO `projectmenbers` VALUES (1, 1, 1, '1', '2025-12-16 18:23:17');

-- ----------------------------
-- Table structure for projects
-- ----------------------------
DROP TABLE IF EXISTS `projects`;
CREATE TABLE `projects`  (
  `project_id` int(11) NOT NULL AUTO_INCREMENT,
  `project_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `team_id` int(11) NULL DEFAULT NULL,
  `project_manager_id` int(11) NULL DEFAULT NULL,
  `start_date` datetime NULL DEFAULT NULL,
  `end_date` datetime NULL DEFAULT NULL,
  `status` enum('规划中','进行中','已暂停','已完成','已取消') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '规划中',
  PRIMARY KEY (`project_id`) USING BTREE,
  INDEX `fk_projects_project_manager_id`(`project_manager_id`) USING BTREE,
  INDEX `fk_projects_team_id`(`team_id`) USING BTREE,
  CONSTRAINT `fk_projects_project_manager_id` FOREIGN KEY (`project_manager_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_projects_team_id` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of projects
-- ----------------------------
INSERT INTO `projects` VALUES (1, '1', 1, 1, '2025-12-16 18:22:51', '2025-12-18 18:22:54', '规划中');
INSERT INTO `projects` VALUES (2, '项目2', 1, 2, '2025-12-16 18:33:21', '2025-12-19 18:33:24', '规划中');

-- ----------------------------
-- Table structure for promotion_requests
-- ----------------------------
DROP TABLE IF EXISTS `promotion_requests`;
CREATE TABLE `promotion_requests`  (
  `request_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `request_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('待审批','已批准','已拒绝') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '待审批',
  `processed_date` datetime NULL DEFAULT NULL,
  `processed_by` int(11) NULL DEFAULT NULL,
  `current_role_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `target_role_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`request_id`) USING BTREE,
  INDEX `fk_promotion_requests_user_id`(`user_id`) USING BTREE,
  INDEX `fk_promotion_requests_processed_by`(`processed_by`) USING BTREE,
  CONSTRAINT `fk_promotion_requests_processed_by` FOREIGN KEY (`processed_by`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_promotion_requests_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of promotion_requests
-- ----------------------------
INSERT INTO `promotion_requests` VALUES (1, 2, '2025-12-23 17:24:35', '已拒绝', '2025-12-23 14:36:37', 1, 'V7', 'V6');
INSERT INTO `promotion_requests` VALUES (10, 2, '2025-12-23 22:29:02', '已拒绝', '2025-12-23 15:36:54', 1, 'V8', 'V7');
INSERT INTO `promotion_requests` VALUES (11, 2, '2025-12-23 22:29:43', '待审批', NULL, NULL, 'V8', 'V7');
INSERT INTO `promotion_requests` VALUES (12, 2, '2025-12-23 22:31:05', '已批准', '2025-12-23 14:34:30', 1, 'V8', 'V7');
INSERT INTO `promotion_requests` VALUES (13, 15, '2025-12-23 23:34:37', '已批准', '2025-12-23 15:36:44', 1, 'V8', 'V7');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `role_name` enum('CEO','董事','经理','总监','组长','工程师','助理','实习生') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '实习生',
  `role_id` enum('V1','V2','V3','V4','V5','V6','V7','V8') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'V8',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('CEO', 'V1');
INSERT INTO `roles` VALUES ('董事', 'V2');
INSERT INTO `roles` VALUES ('经理', 'V3');
INSERT INTO `roles` VALUES ('总监', 'V4');
INSERT INTO `roles` VALUES ('组长', 'V5');
INSERT INTO `roles` VALUES ('工程师', 'V6');
INSERT INTO `roles` VALUES ('助理', 'V7');
INSERT INTO `roles` VALUES ('实习生', 'V8');

-- ----------------------------
-- Table structure for teammenbers
-- ----------------------------
DROP TABLE IF EXISTS `teammenbers`;
CREATE TABLE `teammenbers`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `team_id` int(11) NOT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `join_date` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `role_in_team` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_team_user`(`team_id`, `user_id`) USING BTREE,
  INDEX `fk_teammenbers_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `fk_teammenbers_team_id` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_teammenbers_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teammenbers
-- ----------------------------
INSERT INTO `teammenbers` VALUES (1, 1, 1, '2025-12-14 15:55:34', NULL);
INSERT INTO `teammenbers` VALUES (6, 3, 2, NULL, NULL);

-- ----------------------------
-- Table structure for teams
-- ----------------------------
DROP TABLE IF EXISTS `teams`;
CREATE TABLE `teams`  (
  `team_id` int(11) NOT NULL AUTO_INCREMENT,
  `team_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `team_leader_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`team_id`) USING BTREE,
  INDEX `fk_teams_team_leader_id`(`team_leader_id`) USING BTREE,
  CONSTRAINT `fk_teams_team_leader_id` FOREIGN KEY (`team_leader_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teams
-- ----------------------------
INSERT INTO `teams` VALUES (1, 'def', 1);
INSERT INTO `teams` VALUES (3, 'atp团队', 3);
INSERT INTO `teams` VALUES (4, 'qq', 3);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role_id` enum('V1','V2','V3','V4','V5','V6','V7','V8') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `position_id` int(11) NULL DEFAULT NULL,
  `department_id` int(11) NULL DEFAULT NULL,
  `hire_date` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` enum('在职','离职','休假') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '在职',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_department_id`(`department_id`) USING BTREE,
  CONSTRAINT `fk_users_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'atp', '1234', 'V1', '15859220210', NULL, NULL, '2025-12-07 18:55:49', '在职');
INSERT INTO `users` VALUES (2, '1', '1', 'V7', '3', NULL, NULL, '2025-12-07 13:56:58', '在职');
INSERT INTO `users` VALUES (3, 'qq', '12', 'V4', '111', NULL, NULL, '2025-12-07 18:55:59', '在职');
INSERT INTO `users` VALUES (4, 'Lok On Na', 'x0UTmyQK6U', 'V2', '769-6114-2115', 690, 139, '2012-07-08 07:09:15', '离职');
INSERT INTO `users` VALUES (7, 'Karen Reynolds', 'N7jDsGFiLT', 'V5', '212-425-8517', 704, 709, '2002-03-13 09:14:38', '休假');
INSERT INTO `users` VALUES (8, 'Shi Yunxi', 'WlGhXNKCZQ', 'V3', '213-962-6012', 459, 958, '2001-04-07 17:43:59', '休假');
INSERT INTO `users` VALUES (9, 'Yoshida Airi', 'oT6UTlXI6g', 'V1', '7686 265148', 369, 950, '2004-03-14 08:33:55', '在职');
INSERT INTO `users` VALUES (10, 'Kelly Richardson', 'IEaLz8vIPI', 'V8', '80-5247-0174', 119, 406, '2004-03-21 14:36:11', '在职');
INSERT INTO `users` VALUES (11, 'Cai Jialun', 'U7ON9HP5yl', 'V2', '330-391-8771', 420, 190, '2015-09-02 16:40:19', '离职');
INSERT INTO `users` VALUES (12, 'Hsuan Chi Yuen', 'QOZZH4wj5o', 'V6', '760-689-3944', 735, 418, '2021-09-09 17:05:29', '在职');
INSERT INTO `users` VALUES (13, 'Leung Wai Yee', '14Y0vQQuD9', 'V2', '184-4936-6036', 15, 318, '2020-02-27 01:07:57', '休假');
INSERT INTO `users` VALUES (14, 'admin', 'admin', 'V8', '1234', NULL, NULL, '2025-12-23 15:05:35', '在职');
INSERT INTO `users` VALUES (15, '2', '2', 'V7', '222', NULL, NULL, '2025-12-23 15:33:20', '在职');

SET FOREIGN_KEY_CHECKS = 1;
