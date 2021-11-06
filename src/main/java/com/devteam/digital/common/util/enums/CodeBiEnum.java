package com.devteam.digital.common.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeBiEnum {

    ONE(1, "Old mailbox modify mailbox "),

    TWO(2, "Change password by email ");

    private final Integer code;
    private final String description;

    public static CodeBiEnum find(Integer code) {
        for (CodeBiEnum value : CodeBiEnum.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        return null;
    }

}
