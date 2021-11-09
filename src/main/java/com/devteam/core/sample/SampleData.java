package com.devteam.core.sample;

import com.devteam.module.common.ClientInfo;
import lombok.Getter;

public class SampleData {
  @Getter
  protected ClientInfo client;

  public void init(ClientInfo client) {
    this.client = client;
    initialize(client);
  }

  protected void initialize(ClientInfo client) {
  }
}
