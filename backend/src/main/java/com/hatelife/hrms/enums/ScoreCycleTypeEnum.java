package com.hatelife.hrms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评分周期类型枚举
 */
@Getter
@AllArgsConstructor
public enum ScoreCycleTypeEnum {

    EMPLOYEE(1, "员工考核"),
    LEADER(2, "领导考核");

    private final int code;
    private final String desc;

    public static ScoreCycleTypeEnum of(int code) {
        for (ScoreCycleTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
