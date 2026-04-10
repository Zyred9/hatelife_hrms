package com.hatelife.hrms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OKR 状态枚举
 */
@Getter
@AllArgsConstructor
public enum OkrStatusEnum {

    NORMAL(1, "正常"),
    REVIEWING(2, "审核中"),
    REJECTED(3, "驳回待修改"),
    VOID(4, "作废");

    private final int code;
    private final String desc;

    public static OkrStatusEnum of(int code) {
        for (OkrStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
