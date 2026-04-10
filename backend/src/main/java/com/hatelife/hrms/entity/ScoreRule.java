package com.hatelife.hrms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评分规则维度表
 * <p>
 * 管理员可配置的评分维度，按 cycle_type 区分员工考核和领导考核的不同维度。
 * </p>
 *
 * @author zyred
 * @since 2026-04-10
 */
@Data
@TableName("t_score_rule")
public class ScoreRule {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 周期类型：1员工考核 2领导考核，参见 ScoreCycleTypeEnum
     */
    private Integer cycleType;

    /**
     * 维度名称
     */
    private String dimensionName;

    /**
     * 权重（百分比，如 30.00 表示 30%）
     */
    private java.math.BigDecimal weight;

    /**
     * 维度描述
     */
    private String description;

    /**
     * 排序序号
     */
    private Integer sortOrder;

    /**
     * 是否启用：0禁用 1启用
     */
    private Integer enabled;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
