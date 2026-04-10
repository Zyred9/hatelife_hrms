package com.hatelife.hrms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * OKR审核记录表
 * <p>
 * 记录每次审核操作（通过/驳回/作废），用于审计和追溯。
 * </p>
 *
 * @author zyred
 * @since 2026-04-10
 */
@Data
@TableName("t_okr_review_log")
public class OkrReviewLog {

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
     * 审核人用户ID，关联 t_user.id
     */
    private Long reviewerId;

    /**
     * 操作：1通过 2驳回 3作废，参见 ReviewActionEnum
     */
    private Integer action;

    /**
     * 操作原因/驳回理由
     */
    private String reason;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
