package com.devteam.config.enums;

public enum StorageState {
  CREATED, INACTIVE, JUNK, DEPRECATED, ACTIVE, ARCHIVED;

  static public StorageState[] ALL = StorageState.values();
}