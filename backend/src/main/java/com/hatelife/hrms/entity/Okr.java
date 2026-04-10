package com.hatelife.hrms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * OKR记录表
 * <p>
 * 每位员工可有多条OKR记录，通过 status 字段流转状态（正常→审核中→驳回/正常→评分）。
 * </p>
 *
 * @author zyred
 * @since 2026-04-10
 */
@Data
@TableName("t_okr")
public class Okr {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属用户ID，关联 t_user.id
     */
    private Long userId;

    /**
     * OKR 标题
     */
    private String title;

    /**
     * OKR 内容（Objective + Key Results）
     */
    private String content;

    /**
     * 状态：1正常 2审核中 3驳回待修改 4作废，参见 OkrStatusEnum
     */
    private Integer status;

    /**
     * 驳回原因
     */
    private String reviewReason;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
