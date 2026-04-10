package com.hatelife.hrms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审核操作枚举
 */
@Getter
@AllArgsConstructor
public enum ReviewActionEnum {

    APPROVE(1, "通过"),
    REJECT(2, "驳回"),
    VOID(3, "作废");

    private final int code;
    private final String desc;

    public static ReviewActionEnum of(int code) {
        for (ReviewActionEnum action : values()) {
            if (action.code == code) {
                return action;
            }
        }
        return null;
    }
}
