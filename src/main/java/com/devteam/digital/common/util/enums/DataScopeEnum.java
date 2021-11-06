package com.devteam.digital.common.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataScopeEnum {

    ALL("All", "All data permission"),

    THIS_LEVEL("This level", "Data authority of own department "),

    CUSTOMIZE("Custom", "Custom data permissions");

    private final String value;
    private final String description;

    public static DataScopeEnum find(String val) {
        for (DataScopeEnum dataScopeEnum : DataScopeEnum.values()) {
            if (val.equals(dataScopeEnum.getValue())) {
                return dataScopeEnum;
            }
        }
        return null;
    }

}
