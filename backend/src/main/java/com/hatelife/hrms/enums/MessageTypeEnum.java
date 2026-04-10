package com.hatelife.hrms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型枚举
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

    OKR_FLOW(1, "OKR流程提醒"),
    SCORE_FLOW(2, "评分流程提醒"),
    SYSTEM(3, "系统通知"),
    CARE(4, "关怀话术");

    private final int code;
    private final String desc;

    public static MessageTypeEnum of(int code) {
        for (MessageTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
