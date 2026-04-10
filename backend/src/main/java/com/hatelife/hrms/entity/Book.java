package com.hatelife.hrms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 书籍解读表
 * <p>
 * 存储每天新增的书籍解读记录，包含章节摘要和读书笔记。
 * </p>
 *
 * @author zyred
 * @since 2026-04-10
 */
@Data
@TableName("t_book")
public class Book {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 书名
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 封面图片URL
     */
    private String coverUrl;

    /**
     * 全书摘要
     */
    private String summary;

    /**
     * 章节摘要，JSON数组
     */
    private String chapters;

    /**
     * 解读时间
     */
    private LocalDateTime interpretedAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
