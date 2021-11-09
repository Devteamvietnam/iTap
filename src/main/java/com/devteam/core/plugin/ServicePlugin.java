package com.devteam.core.plugin;

import com.devteam.core.plugin.entity.PluginInfo;
import com.devteam.core.plugin.repository.PluginInfoRepository;
import com.devteam.module.common.ClientInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

abstract public class ServicePlugin {
  @Autowired
  PluginInfoRepository repo ;

  @Getter
  private String module;
  @Getter
  private String service;
  @Getter
  private String type;

  @Getter @Setter
  private String description;

  protected ServicePlugin(String module, String service, String type) {
    this.module  = module;
    this.service = service;
    this.type    = type;
  }

  public PluginInfo createPluginInfo(ClientInfo client) {
    PluginInfo info = new PluginInfo(module, service, type);
    info.setDescription(description);
    info.set(client);
    repo.save(info);
    return info;
  }

  public PluginInfo getPluginInfo(ClientInfo client) {
    return repo.getOne(module, service, type);
  }

}
