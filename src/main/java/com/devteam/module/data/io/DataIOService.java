package com.devteam.module.data.io;

import java.util.List;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.http.upload.UploadResourceRequest;
import com.devteam.module.company.core.entity.Company;
import com.devteam.module.data.io.entity.DataIOPluginImportEntries;
import com.devteam.module.data.io.entity.ProcessedRecordDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataIOService {
  @Autowired
  private DataImportLogic logic;
  
  @Transactional
  public DataIOPluginInfo getPlugin(ClientInfo client, Company company, String module, String pluginName) {
    return logic.getPlugin(client, company, module, pluginName);
  }
  
  @Transactional
  public List<DataIOPluginInfo> getPlugins(ClientInfo client, Company company) {
    return logic.getPlugins(client, company);
  }

  @Transactional
  public DataIOPluginImportEntries getImportEntries(ClientInfo client, Company company, String module, String name) {
    return logic.getImportDescriptor(client, company, module, name);
  }

  @Transactional
  public ImportEntryProcessedResult getImportEntryProcessedResult(ClientInfo client, Company company, String module, String name, String entryName) {
    return logic.getImportEntryProcessedResult(client, company, module, name, entryName);
  }

  @Transactional
  public DataIOPluginImportEntries upload(ClientInfo client, Company company, String module, String name, UploadResourceRequest req) {
    return logic.upload(client, company, module, name, req);
  }
  
  @Transactional
  public DataIOPluginImportEntries remove(ClientInfo client, Company company, String module, String name, UploadResourceRequest req) {
    return logic.remove(client, company, module, name, req);
  }
  
  @Transactional
  public ProcessedRecordDetail getProcessedRecordDetail(ClientInfo client, Company company, String module, String name, String entryName, String groupName, String label) {
    return logic.getProcessedRecordDetail(client, company, module, name, entryName, groupName, label);
  }
}
