-- ============================================
-- US-001: 用户表与上下级关系表
-- ============================================

-- 用户表
CREATE TABLE IF NOT EXISTS `t_user` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`        VARCHAR(64)     NOT NULL COMMENT '登录用户名',
    `password`        VARCHAR(128)    NOT NULL COMMENT '登录密码',
    `name`            VARCHAR(64)     NOT NULL COMMENT '姓名',
    `gender`          TINYINT         NOT NULL DEFAULT 0 COMMENT '性别：0未知 1男 2女',
    `age`             INT             NULL     DEFAULT NULL COMMENT '年龄',
    `phone`           VARCHAR(20)     NOT NULL COMMENT '手机号',
    `department`      VARCHAR(128)    NULL     DEFAULT NULL COMMENT '部门',
    `status`          TINYINT         NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
    `is_admin`        TINYINT         NOT NULL DEFAULT 0 COMMENT '是否管理员：0否 1是',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_department` (`department`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户上下级关系表
CREATE TABLE IF NOT EXISTS `t_user_relation` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `superior_id`     BIGINT          NOT NULL COMMENT '上级用户ID',
    `subordinate_id`  BIGINT          NOT NULL COMMENT '下级用户ID',
    `relation_type`   TINYINT         NOT NULL DEFAULT 1 COMMENT '关系类型：1直属 2间接',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_superior_subordinate` (`superior_id`, `subordinate_id`),
    KEY `idx_superior` (`superior_id`),
    KEY `idx_subordinate` (`subordinate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户上下级关系表';

-- 初始化管理员账号（用户名: gongyuwen, 密码: gongyuwen）
INSERT INTO `t_user` (`username`, `password`, `name`, `gender`, `phone`, `is_admin`, `status`)
VALUES ('gongyuwen', 'gongyuwen', '宫宇文', 0, '00000000000', 1, 1)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `is_admin` = 1;
