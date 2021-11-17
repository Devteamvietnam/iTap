package com.devteam.module.data.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.DAOService;
import com.devteam.core.module.http.upload.UploadResourceRequest;
import com.devteam.core.util.dataformat.DataSerializer;
import com.devteam.module.company.core.entity.Company;
import com.devteam.module.data.io.entity.DataIOPluginImportEntries;
import com.devteam.module.data.io.entity.ProcessedRecordDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DataImportLogic extends DAOService {
  private Map<String, DataIOPlugin> plugins = new HashMap<>();
 
  @Autowired(required = false) 
  public void setPlugins(List<DataIOPlugin> plugins) {
    for(DataIOPlugin plugin : plugins) {
      String key = plugin.getPluginInfo().getModule() + "/" + plugin.getPluginInfo().getPluginName() ;
      this.plugins.put(key, plugin);
    }
  }
  
  public DataIOPluginInfo getPlugin(ClientInfo client, Company company, String module, String pluginName) {
    List<DataIOPluginInfo> plugins = getPlugins(client, company);
    for(DataIOPluginInfo sel : plugins) {
      if (sel.getModule().equals(module) && sel.getPluginName().equals(pluginName)) {
        return sel;
      }
    }
    return null;
  }
  
  public List<DataIOPluginInfo> getPlugins(ClientInfo client, Company company) {
    List<DataIOPluginInfo>  holder = new ArrayList<>();
    for(DataIOPlugin sel : plugins.values()) {
      holder.add(sel.getPluginInfo());
    }
    return holder;
  }

  public DataIOPluginImportEntries getImportDescriptor(ClientInfo client, Company company, String module, String name) {
    DataIOPlugin plugin = getPlugin(module,name);
    return plugin.getImportEntries(client, company);
  }

  public ProcessedRecordDetail getProcessedRecordDetail(
      ClientInfo client, Company company, String module, String name, String entryName, String groupName, String label) {
    DataIOPlugin plugin = getPlugin(module, name);
    return plugin.getProcessedRecordDetails(client, company, entryName, groupName, label);
  }
  
  public ImportEntryProcessedResult getImportEntryProcessedResult(
      ClientInfo client, Company company, String module, String name, String entryName) {
    System.out.println(DataSerializer.JSON.toString(name+"-"+entryName));
    DataIOPlugin plugin = getPlugin(module,name);
    return plugin.getImportEntryProcessedResult(client, company, entryName);
  }

  public DataIOPluginImportEntries upload(ClientInfo client, Company company, String module, String name, UploadResourceRequest req) {
    DataIOPlugin plugin = getPlugin(module,name);
    return plugin.upload(client, company, req);
  }

  public DataIOPluginImportEntries remove(ClientInfo client, Company company, String module, String name, UploadResourceRequest req) {
    DataIOPlugin plugin = getPlugin(module,name);
    return plugin.remove(client, company, req);
  }
  
  public ImportEntryOp process(ClientInfo client, Company company, ImportEntryOp op) {
    DataIOPlugin plugin = getPlugin(op.getModule(), op.getPlugin());
    DataContext ctx = new DataContext();
    ctx.addAttribute(client);
    ctx.addAttribute(company);
    return plugin.process(ctx, op);
  }
  
  DataIOPlugin getPlugin(String module, String name) {
    System.out.println(name);
    String key = module + "/" + name;
    DataIOPlugin plugin = plugins.get(key);
    return plugin;
  }
}