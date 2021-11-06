package com.devteam.digital.common.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeEnum {

    PHONE_RESET_EMAIL_CODE("phone_reset_email_code_", "Reset phone number"),

    EMAIL_RESET_EMAIL_CODE("email_reset_email_code_", "Reset email"),

    PHONE_RESET_PWD_CODE("phone_reset_pwd_code_", "Reset password phone number"),

    EMAIL_RESET_PWD_CODE("email_reset_pwd_code_", "Reset password email");

    private final String key;
    private final String description;
}
