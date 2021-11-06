package com.devteam.digital.common.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestMethodEnum {


    GET("GET"),

    POST("POST"),

    PUT("PUT"),

    PATCH("PATCH"),

    DELETE("DELETE"),

    ALL("All");

    private final String type;

    public static RequestMethodEnum find(String type) {
        for (RequestMethodEnum value : RequestMethodEnum.values()) {
            if (type.equals(value.getType())) {
                return value;
            }
        }
        return ALL;
    }
}
