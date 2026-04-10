package com.hatelife.hrms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 评分记录表
 * <p>
 * 每条记录代表一个评分人对某个OKR的某一轮评分（自评/领导评分/最终评分）。
 * 提交后 locked=1，不可修改。
 * </p>
 *
 * @author zyred
 * @since 2026-04-10
 */
@Data
@TableName("t_score_record")
public class ScoreRecord {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联的 OKR ID，关联 t_okr.id
     */
    private Long okrId;

    /**
     * 评分人用户ID，关联 t_user.id
     */
    private Long reviewerId;

    /**
     * 评分类型：1自评 2领导评分 3最终评分，参见 ReviewTypeEnum
     */
    private Integer reviewType;

    /**
     * 总分（维度分 × 权重之和）
     */
    private BigDecimal totalScore;

    /**
     * 各维度得分明细，JSON格式
     */
    private String dimensionScores;

    /**
     * 是否锁定：0未锁定 1已锁定
     */
    private Integer locked;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
