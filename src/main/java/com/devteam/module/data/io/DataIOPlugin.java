package com.devteam.module.data.io;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.http.upload.UploadResource;
import com.devteam.core.module.http.upload.UploadResourceRequest;
import com.devteam.core.util.ds.Collections;
import com.devteam.core.util.error.ErrorType;
import com.devteam.core.util.error.RuntimeError;
import com.devteam.core.util.text.StringUtil;
import com.devteam.module.company.core.entity.Company;
import com.devteam.module.data.io.entity.DataIOPluginImportEntries;
import com.devteam.module.data.io.entity.ImportEntry;
import com.devteam.module.data.io.entity.ProcessedRecord;
import com.devteam.module.data.io.entity.ProcessedRecordDetail;
import com.devteam.module.data.io.repository.ImportEntryRepository;
import com.devteam.module.data.io.repository.ProcessedRecordDetailRepository;
import com.devteam.module.data.io.repository.ProcessedRecordRepository;
import com.devteam.module.storage.CompanyStorage;
import com.devteam.module.storage.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
abstract public class DataIOPlugin {
  @Getter @Setter
  private DataIOPluginInfo pluginInfo;
  
  @Autowired
  private ImportEntryRepository entryRepo;
  
  @Autowired
  private ProcessedRecordDetailRepository processedRecordDetailRepo;
  
  @Autowired
  private ProcessedRecordRepository processedRecordRepo;

  @Autowired
  private IStorageService storageService;

  protected DataIOPlugin(String module, String name, String[] supportDataTypes, String desc) {
    this.pluginInfo = new DataIOPluginInfo(module, name);
    this.pluginInfo.setSupportDataTypes(supportDataTypes);
    this.pluginInfo.setDescription(desc);
  }
  
  public ImportEntry getImportEntry(ClientInfo client, Company company, String entryName) {
    return entryRepo.getByEntryName(company.getId(), pluginInfo.getModule(), pluginInfo.getPluginName(), entryName);
  }
  
  public List<ImportEntry> findImportEntries(ClientInfo client, Company company) {
    return entryRepo.findImportEntries(company.getId(), pluginInfo.getModule(), pluginInfo.getPluginName());
  }
  
  public ImportEntry saveImportEntry(ClientInfo client, Company company, ImportEntry entry) {
    entry.set(client, company);
    return entryRepo.save(entry);
  }
  
  public ProcessedRecordDetail saveProcessedRecord(ClientInfo client, Company company, ImportEntry entry, ProcessedRecordDetail rec) {
    rec.setEntryId(entry.getId());
    rec.set(client, company);
    return processedRecordDetailRepo.save(rec);
  }
  
  public List<ProcessedRecordDetail> saveProcessedRecords(
      ClientInfo client, Company company, ImportEntry entry, List<ProcessedRecordDetail> recs) {
    Long entryId = entry.getId();
    Long companyId = company.getId();
    String groupName = Collections.isNotEmpty(recs) ? recs.get(0).getGroupName() : StringUtil.EMPTY;
    
    List<ProcessedRecordDetail> existGroupRecords = processedRecordDetailRepo.findByGroup(companyId, entryId, groupName);
    if (Collections.isNotEmpty(existGroupRecords)) {
      processedRecordDetailRepo.deleteAllById(existGroupRecords.stream().map(ProcessedRecordDetail::getId).collect(Collectors.toList()));
    }
    
    for(ProcessedRecordDetail rec : recs) {
      rec.setEntryId(entryId);
      rec.set(client, company);
    }
    return processedRecordDetailRepo.saveAll(recs);
  }
  
  public DataIOPluginImportEntries getImportEntries(ClientInfo client, Company company) {
    DataIOPluginImportEntries descriptor = new DataIOPluginImportEntries();
    List<ImportEntry> entries = 
        entryRepo.findImportEntries(company.getId(), pluginInfo.getModule(), this.pluginInfo.getPluginName());
    descriptor.setImportEntries(entries);
    return descriptor;
  }

  public DataIOPluginImportEntries upload(ClientInfo client, Company company, UploadResourceRequest req) {
    String pluginStoragePath = getPluginStoragePath() ;
    CompanyStorage storage = storageService.createCompanyStorage(client, company.getCode());
    if(!storage.hasNode(pluginStoragePath)) {
      initPluginStorage(client, company);
    }
    DataIOPluginImportEntries importEntries = getImportEntries(client, company);
    List<UploadResource> uploadResources = req.getUploadResources() ;
    String storagePath = getUploadStoragePath();
    storage.uploadDep(storagePath, uploadResources);
    for(UploadResource res : uploadResources) {
      ImportEntry entry = importEntries.getImportEntry(res.getName(), true);
      entry.withUploadResource(pluginInfo.getModule(), pluginInfo.getPluginName(), res);
      saveImportEntry(client, company, entry) ;
    }
    return importEntries;
  }
  
  public DataIOPluginImportEntries remove(ClientInfo client, Company company, UploadResourceRequest req) {
    List<UploadResource> uploadResources = req.getUploadResources();
    DataIOPluginImportEntries importEntries = getImportEntries(client, company);
    for(UploadResource res : uploadResources) {
      String resource =  res.getName();
      ImportEntry entry = importEntries.getImportEntry(resource, false);
      importEntries.removeImportEntry(resource);
      processRemove(client, company, entry);
      entryRepo.delete(entry);
    }
    return importEntries;
  }
  
  public ImportEntryProcessedResult getImportEntryProcessedResult(ClientInfo client, Company company, String entryName) {
    ImportEntry entry = getImportEntry(client, company, entryName);
    List<ProcessedRecord> records = processedRecordRepo.findByEntry(company.getId(), entry.getId());
    ImportEntryProcessedResult result = new ImportEntryProcessedResult(entry);
    result.addAll(records);
    return result;
  }
  
  public ProcessedRecordDetail getProcessedRecordDetails(ClientInfo client, Company company, String entryName, String groupName, String label){
    ImportEntry entry = getImportEntry(client, company, entryName);
    return processedRecordDetailRepo.findByLabel(company.getId(), entry.getId(), groupName, label);
  }
  
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public ImportEntryOp process(DataContext ctx, ImportEntryOp op) {
    ClientInfo client = ctx.getAttribute(ClientInfo.class);
    Company company = ctx.getAttribute(Company.class);
    ImportEntry entry = getImportEntry(client, company, op.getEntryName());
    if(entry == null) {
      throw new RuntimeError(ErrorType.IllegalArgument, "Cannot find the entry " + op.getEntryName());
    }
    if(op.getType() == ImportEntryOp.ImportEntryOpType.Validate) {
      execProcessValidate(ctx, client, company, entry) ;
    } else if(op.getType() == ImportEntryOp.ImportEntryOpType.Import) {
      ctx.addAttribute(op.getMode());
      execProcessImport(ctx, client, company, entry) ;
      entry.incrImportCount(1);
    } else if(op.getType() == ImportEntryOp.ImportEntryOpType.Remove) {
      processRemove(client, company, entry);
    }
    return op;
  }

  protected List<ImportEntry> saveImportEntries(ClientInfo client, Company company, List<ImportEntry> entries) {
    for(ImportEntry entry : entries) {
      entry.set(client, company);
    }
    entries = entryRepo.saveAll(entries);
    return entries;
  }

  void execProcessValidate(DataContext ctx, ClientInfo client, Company company, ImportEntry entry) {
    entry.setProcessedStatus("VALIDATE PROCESSING");
    saveImportEntry(client, company, entry);
    
    long startTime = System.currentTimeMillis();
    processValidate(ctx, entry);
    
    
    long endTime = System.currentTimeMillis();
    entry.setProcessedAt(new Date(startTime));
    entry.setProcessedEnd(new Date(endTime));
    entry.setExecutionTime(endTime - startTime);
 
    entry.setProcessedStatus("VALIDATE DONE");
    saveImportEntry(client, company, entry);
  }

  void execProcessImport(DataContext ctx, ClientInfo client, Company company, ImportEntry entry) {
    entry.setProcessedStatus("IMPORT PROCESSING");
    
    long startTime = System.currentTimeMillis();
    processImport(ctx, entry);
    long endTime = System.currentTimeMillis();
    entry.setProcessedAt(new Date(startTime));
    entry.setProcessedEnd(new Date(endTime));
    entry.setExecutionTime(endTime - startTime);
    
    entry.setProcessedStatus("IMPORT DONE");
    entry.incrImportCount(1);
    saveImportEntry(client, company, entry);
  }

  abstract protected void processValidate(DataContext ctx, ImportEntry entry) ;
  abstract protected void processImport(DataContext ctx, ImportEntry entry) ;

  protected void processRemove(ClientInfo client, Company company, ImportEntry entry) {
    CompanyStorage storage = storageService.createCompanyStorage(client, company.getCode());
    storage.delete(getUploadStoragePath(entry));
  }
 
  protected InputStream getEntryDataAsInputStream(ClientInfo client, Company company, ImportEntry entry) {
    String path = getUploadStoragePath(entry);
    CompanyStorage storage = storageService.createCompanyStorage(client, company.getCode());
    return storage.getContentAsInputStream(path);
  }
  
  void initPluginStorage(ClientInfo client, Company company) {
    CompanyStorage storage = storageService.createCompanyStorage(client, company.getCode());
    storage.initDirectory(getUploadStoragePath());
  }
  
  String getPluginStoragePath() { return "apps/data-import/" + pluginInfo.getModule() + "/" + pluginInfo.getPluginName() ;  }

  String getUploadStoragePath() { return getPluginStoragePath() + "/upload"; }

  String getUploadStoragePath(ImportEntry entry) { 
    return getUploadStoragePath() + "/" + entry.getFileName(); 
  }
}
