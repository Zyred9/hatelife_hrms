-- ============================================
-- US-002: OKR 与评分表
-- ============================================

-- OKR 记录表
CREATE TABLE IF NOT EXISTS `t_okr` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`         BIGINT          NOT NULL COMMENT '所属用户ID，关联 t_user.id',
    `title`           VARCHAR(256)    NOT NULL COMMENT 'OKR 标题',
    `content`         TEXT            NOT NULL COMMENT 'OKR 内容（Objective + Key Results）',
    `status`          TINYINT         NOT NULL DEFAULT 1 COMMENT '状态：1正常 2审核中 3驳回待修改 4作废，参见 OkrStatusEnum',
    `review_reason`   VARCHAR(512)    NULL     DEFAULT NULL COMMENT '驳回原因',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OKR记录表';

-- 评分规则表
CREATE TABLE IF NOT EXISTS `t_score_rule` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `cycle_type`      TINYINT         NOT NULL COMMENT '周期类型：1员工考核 2领导考核，参见 ScoreCycleTypeEnum',
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

-- 评分记录表
CREATE TABLE IF NOT EXISTS `t_score_record` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `okr_id`          BIGINT          NOT NULL COMMENT '关联的 OKR ID，关联 t_okr.id',
    `reviewer_id`     BIGINT          NOT NULL COMMENT '评分人用户ID，关联 t_user.id',
    `review_type`     TINYINT         NOT NULL COMMENT '评分类型：1自评 2领导评分 3最终评分，参见 ReviewTypeEnum',
    `total_score`     DECIMAL(10,2)   NULL     DEFAULT NULL COMMENT '总分（维度分 × 权重之和）',
    `dimension_scores` JSON           NULL     DEFAULT NULL COMMENT '各维度得分明细，JSON格式：[{"dimensionId":1,"score":85},{"dimensionId":2,"score":90}]',
    `locked`          TINYINT         NOT NULL DEFAULT 0 COMMENT '是否锁定：0未锁定 1已锁定（提交后不可修改）',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_okr_reviewer_type` (`okr_id`, `reviewer_id`, `review_type`),
    KEY `idx_okr_id` (`okr_id`),
    KEY `idx_reviewer_id` (`reviewer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评分记录表';

-- OKR 审核记录表
CREATE TABLE IF NOT EXISTS `t_okr_review_log` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `okr_id`          BIGINT          NOT NULL COMMENT '关联的 OKR ID，关联 t_okr.id',
    `reviewer_id`     BIGINT          NOT NULL COMMENT '审核人用户ID，关联 t_user.id',
    `action`          TINYINT         NOT NULL COMMENT '操作：1通过 2驳回 3作废，参见 ReviewActionEnum',
    `reason`          VARCHAR(512)    NULL     DEFAULT NULL COMMENT '操作原因/驳回理由',
    `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_okr_id` (`okr_id`),
    KEY `idx_reviewer_id` (`reviewer_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OKR审核记录表';
