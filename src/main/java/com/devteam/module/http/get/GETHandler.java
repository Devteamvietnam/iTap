package com.devteam.module.http.get;

import com.devteam.config.ClientInfo;
import lombok.Getter;

abstract public class GETHandler {
  @Getter
  private String name;

  protected GETHandler(String name) {
    this.name = name;
  }

  abstract public GETContent get(ClientInfo client, String path) ;

  public GETContent get(String path) {
    throw new RuntimeException("Need to implement this method");

  }
}
