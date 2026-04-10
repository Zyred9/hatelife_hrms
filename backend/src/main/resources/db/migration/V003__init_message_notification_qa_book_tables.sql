-- ============================================
-- US-003: 消息、通知配置、助手问答、书籍表
-- ============================================

-- 站内消息表
CREATE TABLE IF NOT EXISTS `t_message` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`         BIGINT          NOT NULL COMMENT '接收用户ID，关联 t_user.id',
    `sender_id`       BIGINT          NULL     DEFAULT NULL COMMENT '发送用户ID，关联 t_user.id，系统消息为NULL',
    `title`           VARCHAR(256)    NOT NULL COMMENT '消息标题',
    `content`         TEXT            NOT NULL COMMENT '消息内容',
    `type`            TINYINT         NOT NULL DEFAULT 1 COMMENT '消息类型：1OKR流程提醒 2评分流程提醒 3系统通知 4关怀话术，参见 MessageTypeEnum',
    `is_read`         TINYINT         NOT NULL DEFAULT 0 COMMENT '是否已读：0未读 1已读',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='站内消息表';

-- 通知提醒配置表
CREATE TABLE IF NOT EXISTS `t_notification_config` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `config_key`      VARCHAR(128)    NOT NULL COMMENT '配置唯一标识',
    `enabled`         TINYINT         NOT NULL DEFAULT 1 COMMENT '是否启用：0禁用 1启用',
    `target_user_groups` JSON         NULL     DEFAULT NULL COMMENT '目标用户组，JSON数组：[1,2] 对应用户组ID',
    `description`     VARCHAR(256)    NULL     DEFAULT NULL COMMENT '配置描述',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知提醒配置表';

-- AI助手问题库表
CREATE TABLE IF NOT EXISTS `t_assistant_qa` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `question`        VARCHAR(512)    NOT NULL COMMENT '问题',
    `answer`          TEXT            NOT NULL COMMENT '答案',
    `assistant_type`  TINYINT         NOT NULL COMMENT '助手类型：1根宝(银渐层) 2多多(布偶) 3蛋挞(金渐层)，参见 AssistantTypeEnum',
    `tags`            VARCHAR(256)    NULL     DEFAULT NULL COMMENT '标签，逗号分隔',
    `enabled`         TINYINT         NOT NULL DEFAULT 1 COMMENT '是否启用：0禁用 1启用',
    `sort_order`      INT             NOT NULL DEFAULT 0 COMMENT '排序序号',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_assistant_type` (`assistant_type`),
    KEY `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI助手问题库表';

-- 书籍解读表
CREATE TABLE IF NOT EXISTS `t_book` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title`           VARCHAR(256)    NOT NULL COMMENT '书名',
    `author`          VARCHAR(128)    NULL     DEFAULT NULL COMMENT '作者',
    `cover_url`       VARCHAR(512)    NULL     DEFAULT NULL COMMENT '封面图片URL',
    `summary`         TEXT            NULL     DEFAULT NULL COMMENT '全书摘要',
    `chapters`        JSON            NULL     DEFAULT NULL COMMENT '章节摘要，JSON数组：[{"title":"第一章","summary":"...","notes":"..."}]',
    `interpreted_at`  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '解读时间',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_title` (`title`),
    KEY `idx_interpreted_at` (`interpreted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='书籍解读表';
