-- ============================================
-- 折耳根绩效管理平台 - 完整数据库初始化脚本
-- 执行：mysql -u root -p hatelife_hrms < schema_init.sql
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- US-001: 用户表与上下级关系表
-- ============================================

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

-- ============================================
-- US-002: OKR 与评分表
-- ============================================

CREATE TABLE IF NOT EXISTS `t_okr` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`         BIGINT          NOT NULL COMMENT '所属用户ID，关联 t_user.id',
    `title`           VARCHAR(256)    NOT NULL COMMENT 'OKR 标题',
    `content`         TEXT            NOT NULL COMMENT 'OKR 内容（Objective + Key Results）',
    `status`          TINYINT         NOT NULL DEFAULT 1 COMMENT '状态：1正常 2审核中 3驳回待修改 4作废',
    `review_reason`   VARCHAR(512)    NULL     DEFAULT NULL COMMENT '驳回原因',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OKR记录表';

CREATE TABLE IF NOT EXISTS `t_score_rule` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `cycle_type`      TINYINT         NOT NULL COMMENT '周期类型：1员工考核 2领导考核',
    `dimension_name`  VARCHAR(128)    NOT NULL COMMENT '维度名称',
    `weight`          DECIMAL(5,2)    NOT NULL COMMENT '权重（百分比，如 30.00 表示 30%）',
    `description`     VARCHAR(512)    NULL     DEFAULT NULL COMMENT '维度描述',
    `sort_order`      INT             NOT NULL DEFAULT 0 COMMENT '排序序号',
    `enabled`         TINYINT         NOT NULL DEFAULT 1 COMMENT '是否启用：0禁用 1启用',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_cycle_type` (`cycle_type`),
    KEY `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评分规则维度表';

CREATE TABLE IF NOT EXISTS `t_score_record` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `okr_id`          BIGINT          NOT NULL COMMENT '关联的 OKR ID',
    `reviewer_id`     BIGINT          NOT NULL COMMENT '评分人用户ID',
    `review_type`     TINYINT         NOT NULL COMMENT '评分类型：1自评 2领导评分 3最终评分',
    `total_score`     DECIMAL(10,2)   NULL     DEFAULT NULL COMMENT '总分',
    `dimension_scores` JSON           NULL     DEFAULT NULL COMMENT '各维度得分明细 JSON',
    `locked`          TINYINT         NOT NULL DEFAULT 0 COMMENT '是否锁定：0未锁定 1已锁定',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_okr_reviewer_type` (`okr_id`, `reviewer_id`, `review_type`),
    KEY `idx_okr_id` (`okr_id`),
    KEY `idx_reviewer_id` (`reviewer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评分记录表';

CREATE TABLE IF NOT EXISTS `t_okr_review_log` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `okr_id`          BIGINT          NOT NULL COMMENT '关联的 OKR ID',
    `reviewer_id`     BIGINT          NOT NULL COMMENT '审核人用户ID',
    `action`          TINYINT         NOT NULL COMMENT '操作：1通过 2驳回 3作废',
    `reason`          VARCHAR(512)    NULL     DEFAULT NULL COMMENT '操作原因/驳回理由',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_okr_id` (`okr_id`),
    KEY `idx_reviewer_id` (`reviewer_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OKR审核记录表';

-- ============================================
-- US-003: 消息、通知配置、助手问答、书籍表
-- ============================================

CREATE TABLE IF NOT EXISTS `t_message` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`         BIGINT          NOT NULL COMMENT '接收用户ID',
    `sender_id`       BIGINT          NULL     DEFAULT NULL COMMENT '发送用户ID，系统消息为NULL',
    `title`           VARCHAR(256)    NOT NULL COMMENT '消息标题',
    `content`         TEXT            NOT NULL COMMENT '消息内容',
    `type`            TINYINT         NOT NULL DEFAULT 1 COMMENT '消息类型：1OKR流程 2评分流程 3系统 4关怀',
    `is_read`         TINYINT         NOT NULL DEFAULT 0 COMMENT '是否已读：0未读 1已读',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='站内消息表';

CREATE TABLE IF NOT EXISTS `t_notification_config` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `config_key`      VARCHAR(128)    NOT NULL COMMENT '配置唯一标识',
    `enabled`         TINYINT         NOT NULL DEFAULT 1 COMMENT '是否启用：0禁用 1启用',
    `target_user_groups` JSON         NULL     DEFAULT NULL COMMENT '目标用户组 JSON数组',
    `description`     VARCHAR(256)    NULL     DEFAULT NULL COMMENT '配置描述',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知提醒配置表';

CREATE TABLE IF NOT EXISTS `t_assistant_qa` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `question`        VARCHAR(512)    NOT NULL COMMENT '问题',
    `answer`          TEXT            NOT NULL COMMENT '答案',
    `assistant_type`  TINYINT         NOT NULL COMMENT '助手类型：1根宝 2多多 3蛋挞',
    `tags`            VARCHAR(256)    NULL     DEFAULT NULL COMMENT '标签，逗号分隔',
    `enabled`         TINYINT         NOT NULL DEFAULT 1 COMMENT '是否启用：0禁用 1启用',
    `sort_order`      INT             NOT NULL DEFAULT 0 COMMENT '排序序号',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_assistant_type` (`assistant_type`),
    KEY `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI助手问题库表';

CREATE TABLE IF NOT EXISTS `t_book` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title`           VARCHAR(256)    NOT NULL COMMENT '书名',
    `author`          VARCHAR(128)    NULL     DEFAULT NULL COMMENT '作者',
    `cover_url`       VARCHAR(512)    NULL     DEFAULT NULL COMMENT '封面图片URL',
    `summary`         TEXT            NULL     DEFAULT NULL COMMENT '全书摘要',
    `chapters`        JSON            NULL     DEFAULT NULL COMMENT '章节摘要 JSON数组',
    `interpreted_at`  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '解读时间',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_title` (`title`),
    KEY `idx_interpreted_at` (`interpreted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='书籍解读表';

SET FOREIGN_KEY_CHECKS = 1;
