package com.devteam.core.plugin;

import com.devteam.config.ClientInfo;
import com.devteam.core.plugin.entity.PluginInfo;
import com.devteam.core.plugin.repository.PluginInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePluginService {
  @Autowired
  private PluginInfoRepository repo;

  public PluginInfo getPlugin(ClientInfo client, String module, String service, String type) {
    return repo.getOne(module, service, type);
  }

  public List<PluginInfo> find(ClientInfo client, String module, String service) {
    return repo.findPluginInfos(module, service);
  }

  public List<PluginInfo> findAll(ClientInfo client) {
    return repo.findAll();
  }
}
