package com.devteam.core.data.db.entity;

public enum EditMode {
    DRAFT, VALIDATED, LOCKED;

    static public EditMode[] ALL = EditMode.values();
}