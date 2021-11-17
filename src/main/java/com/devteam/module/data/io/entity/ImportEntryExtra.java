package com.devteam.module.data.io.entity;

import java.util.ArrayList;
import java.util.List;

import com.devteam.module.data.io.ImportEntryProcessedResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class ImportEntryExtra {
  private List<Log> processedLogs;
  
  public ImportEntryExtra withProcessedResults(ImportEntryProcessedResult results) {
    clearProcessedInfo();
    return this;
  }

  public <T> ImportEntryExtra withError(Throwable error) {
    clearProcessedInfo();
    Log log = new Log(Log.Level.Error, error.getMessage());
    processedLogs.add(log);
    return this;
  }

  public void addProcessedLog(List<Log> logs) {
    if(logs == null || logs.size() == 0) return;
    if(processedLogs == null) processedLogs = new ArrayList<>();
    processedLogs.addAll(logs);
  }
  
  private void clearProcessedInfo() {
    processedLogs = new ArrayList<>();
  }
}