package com.hatelife.hrms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评分类型枚举
 */
@Getter
@AllArgsConstructor
public enum ReviewTypeEnum {

    SELF(1, "自评"),
    LEADER(2, "领导评分"),
    FINAL(3, "最终评分");

    private final int code;
    private final String desc;

    public static ReviewTypeEnum of(int code) {
        for (ReviewTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
