package com.devteam.digital.common.util;

public interface CallBack {
    void executor();

    default String getCallBackName() {
        return Thread.currentThread().getId() + ":" + this.getClass().getName();
    }
}
