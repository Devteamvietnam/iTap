package com.devteam.core.enums;

public enum StorageState {
  CREATED, INACTIVE, JUNK, DEPRECATED, ACTIVE, ARCHIVED;

  static public StorageState[] ALL = StorageState.values();
}