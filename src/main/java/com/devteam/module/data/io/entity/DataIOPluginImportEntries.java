package com.devteam.module.data.io.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.devteam.core.util.error.ErrorType;
import com.devteam.core.util.error.RuntimeError;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class DataIOPluginImportEntries {
  @JsonIgnore
  private Map<String, ImportEntry> entryMap = new HashMap<>();

  public ImportEntry getImportEntry(String name, boolean createDefault) {
    ImportEntry entry = entryMap.get(name);
    if(entry == null && createDefault) {
      entry = new ImportEntry();
      entry.setFileName(name);
      entryMap.put(name, entry);
    }
    return entry;
  }

  public List<ImportEntry> getImportEntry(String[] names) {
    List<ImportEntry> holder = new ArrayList<>();
    for(String sel : names) {
      ImportEntry entry = entryMap.get(sel);
      if(entry == null) {
        throw new RuntimeError(ErrorType.IllegalArgument, "Cannot find the entry " + sel);
      }
      holder.add(entry);
    }
    return holder;
  }

  public ImportEntry removeImportEntry(String name) {
    ImportEntry entry = entryMap.remove(name);
    return entry;
  }
  
  public void putImportEntry(ImportEntry entry) {
    entryMap.put(entry.getFileName(), entry);
  }
  
  public List<ImportEntry> getImportEntries() {
    List<ImportEntry> entries = new ArrayList<>();
    entries.addAll(entryMap.values());
    return entries;
  }

  public void setImportEntries(List<ImportEntry> entries) {
    for(ImportEntry sel : entries) {
      entryMap.put(sel.getFileName(), sel);
    }
  }
  
}
