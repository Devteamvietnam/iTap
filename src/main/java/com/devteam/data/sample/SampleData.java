package com.devteam.data.sample;

import com.devteam.config.ClientInfo;
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
