package com.hatelife.hrms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI助手类型枚举
 */
@Getter
@AllArgsConstructor
public enum AssistantTypeEnum {

    GENBAO(1, "根宝(银渐层)"),
    DUODUO(2, "多多(布偶)"),
    DANTA(3, "蛋挞(金渐层)");

    private final int code;
    private final String desc;

    public static AssistantTypeEnum of(int code) {
        for (AssistantTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
