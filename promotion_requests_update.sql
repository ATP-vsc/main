-- 职务异变功能数据库表结构更新脚本
-- 为promotion_requests表添加处理日期和处理人字段

-- 添加processed_date（处理日期）和processed_by（处理人ID）字段到promotion_requests表
-- 适用于已存在的表结构更新

-- 添加processed_date字段，用于记录申请处理时间
ALTER TABLE `promotion_requests` 
ADD COLUMN `processed_date` datetime NULL DEFAULT NULL AFTER `status`;

-- 添加processed_by字段，用于记录处理人ID
ALTER TABLE `promotion_requests` 
ADD COLUMN `processed_by` int(11) NULL DEFAULT NULL AFTER `processed_date`;

-- 为processed_by字段添加外键约束，关联到users表
ALTER TABLE `promotion_requests` 
ADD INDEX `fk_promotion_requests_processed_by` (`processed_by`);

ALTER TABLE `promotion_requests` 
ADD CONSTRAINT `fk_promotion_requests_processed_by` 
FOREIGN KEY (`processed_by`) REFERENCES `users` (`id`) 
ON DELETE SET NULL ON UPDATE CASCADE;

-- 更新表注释
ALTER TABLE `promotion_requests` COMMENT = '升职申请表，用于职务异变管理';

-- 首先检查表是否存在
CREATE TABLE IF NOT EXISTS `promotion_requests` (
  `request_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `request_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('待审批','已批准','已拒绝') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '待审批',
  `current_role_id` varchar(10) NULL DEFAULT NULL,
  `target_role_id` varchar(10) NULL DEFAULT NULL,
  `processed_date` datetime NULL DEFAULT NULL,
  `processed_by` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`request_id`) USING BTREE,
  INDEX `fk_promotion_requests_user_id`(`user_id`) USING BTREE,
  INDEX `fk_promotion_requests_processed_by`(`processed_by`) USING BTREE,
  CONSTRAINT `fk_promotion_requests_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_promotion_requests_processed_by` FOREIGN KEY (`processed_by`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- 如果表已存在但缺少字段，则添加这些字段
-- 添加processed_date字段
SET @s = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE table_name = 'promotion_requests' 
     AND column_name = 'processed_date'
     AND table_schema = DATABASE()
    ) > 0,
    "SELECT 1",
    "ALTER TABLE `promotion_requests` ADD COLUMN `processed_date` datetime NULL DEFAULT NULL AFTER `status`"
));
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加processed_by字段
SET @s = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE table_name = 'promotion_requests' 
     AND column_name = 'processed_by'
     AND table_schema = DATABASE()
    ) > 0,
    "SELECT 1",
    "ALTER TABLE `promotion_requests` ADD COLUMN `processed_by` int(11) NULL DEFAULT NULL AFTER `processed_date`, ADD INDEX `fk_promotion_requests_processed_by` (`processed_by`), ADD CONSTRAINT `fk_promotion_requests_processed_by` FOREIGN KEY (`processed_by`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE"
));
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;