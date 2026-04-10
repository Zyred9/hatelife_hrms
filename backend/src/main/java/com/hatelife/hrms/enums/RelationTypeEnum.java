package com.hatelife.hrms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 关系类型枚举
 */
@Getter
@AllArgsConstructor
public enum RelationTypeEnum {

    DIRECT(1, "直属"),
    INDIRECT(2, "间接");

    private final int code;
    private final String desc;

    public static RelationTypeEnum of(int code) {
        for (RelationTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
