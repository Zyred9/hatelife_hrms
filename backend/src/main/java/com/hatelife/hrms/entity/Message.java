package com.hatelife.hrms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 站内消息表
 * <p>
 * 存储系统通知、流程提醒、关怀话术等消息。sender_id 为空表示系统消息。
 * </p>
 *
 * @author zyred
 * @since 2026-04-10
 */
@Data
@TableName("t_message")
public class Message {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 接收用户ID，关联 t_user.id
     */
    private Long userId;

    /**
     * 发送用户ID，关联 t_user.id，系统消息为NULL
     */
    private Long senderId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型：1OKR流程提醒 2评分流程提醒 3系统通知 4关怀话术，参见 MessageTypeEnum
     */
    private Integer type;

    /**
     * 是否已读：0未读 1已读
     */
    private Integer isRead;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
