package com.hatelife.hrms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户上下级关系表
 * <p>
 * 支持多层级、多对多的上下级关系。每条记录表示一个 superior → subordinate 的有向关系。
 * </p>
 *
 * @author zyred
 * @since 2026-04-10
 */
@Data
@TableName("t_user_relation")
public class UserRelation {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 上级用户ID，关联 t_user.id
     */
    private Long superiorId;

    /**
     * 下级用户ID，关联 t_user.id
     */
    private Long subordinateId;

    /**
     * 关系类型：1直属 2间接，参见 RelationTypeEnum
     */
    private Integer relationType;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
