package com.devteam.module.data.io.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class Log {
  static public enum Level { Info, Warn, Error }

  private Level  level;
  private String    log;
  
  public Log(Level level, String log) {
    this.level = level;
    this.log   = log;
  }
}