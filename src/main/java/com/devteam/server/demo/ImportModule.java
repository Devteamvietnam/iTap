package com.devteam.server.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public enum ImportModule {
  COMPANY("company", ImportPlugin.EMPLOYEE),
  SETTINGS("settings", ImportPlugin.SETTINGS),
  ACCOUNT("account", ImportPlugin.ACCOUNT);

  @Getter@Setter
  private String name;

  public List<ImportPlugin> plugins;

  ImportModule(String name, ImportPlugin ... plugins) {
    this.name = name;
    this.plugins = new ArrayList<>();
    for (ImportPlugin plugin : plugins) {
      this.plugins.add(plugin);
    }
  }

  public enum ImportPlugin {
    SETTINGS("Settings"), EMPLOYEE("Employee"), ACCOUNT("Account"), ASSET("Asset");

    @Getter@Setter
    private String name;

    ImportPlugin(String name) {
      this.name = name;
    }
  }
}