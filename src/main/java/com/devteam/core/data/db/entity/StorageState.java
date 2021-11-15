package com.devteam.core.data.db.entity;

public enum StorageState {
    CREATED, INACTIVE, JUNK, DEPRECATED, ACTIVE, ARCHIVED;

    static public StorageState[] ALL = StorageState.values();
}
