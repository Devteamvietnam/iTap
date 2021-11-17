package com.devteam.module.data.io;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class DataIOPluginInfo {
  @Getter @Setter
  private String module;

  @Getter @Setter
  private String pluginName;

  @Getter @Setter
  private String[] supportDataTypes;

  @Getter @Setter
  private String description;
  
  public DataIOPluginInfo(String module, String pluginName) {
    this.module     = module;
    this.pluginName = pluginName;
  }
}
