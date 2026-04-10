package com.hatelife.hrms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知提醒配置表
 * <p>
 * 管理员配置各类消息提醒的开关、目标用户组等。
 * </p>
 *
 * @author zyred
 * @since 2026-04-10
 */
@Data
@TableName("t_notification_config")
public class NotificationConfig {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 配置唯一标识
     */
    private String configKey;

    /**
     * 是否启用：0禁用 1启用
     */
    private Integer enabled;

    /**
     * 目标用户组，JSON数组
     */
    private String targetUserGroups;

    /**
     * 配置描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
