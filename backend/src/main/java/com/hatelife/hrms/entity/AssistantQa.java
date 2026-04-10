package com.hatelife.hrms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI助手问题库表
 * <p>
 * 管理员可配置的问题库，用于 AI 助手问答常见问题。
 * </p>
 *
 * @author zyred
 * @since 2026-04-10
 */
@Data
@TableName("t_assistant_qa")
public class AssistantQa {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 问题
     */
    private String question;

    /**
     * 答案
     */
    private String answer;

    /**
     * 助手类型：1根宝(银渐层) 2多多(布偶) 3蛋挞(金渐层)，参见 AssistantTypeEnum
     */
    private Integer assistantType;

    /**
     * 标签，逗号分隔
     */
    private String tags;

    /**
     * 是否启用：0禁用 1启用
     */
    private Integer enabled;

    /**
     * 排序序号
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
