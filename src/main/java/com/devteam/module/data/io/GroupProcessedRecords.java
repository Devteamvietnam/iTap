package com.devteam.module.data.io;

import java.util.ArrayList;
import java.util.List;

import com.devteam.module.data.io.entity.BaseProcessedRecord;
import com.devteam.module.data.io.entity.ProcessedRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class GroupProcessedRecords {
  private String                   name;
  private List<ProcessedRecord> processedRecords;
  
  public GroupProcessedRecords(String name) {
    this.name = name;
    this.processedRecords = new ArrayList<>();
  }
  
  public GroupProcessedRecords(String name, List<ProcessedRecord> processedRecords) {
    this.name = name;
    this.processedRecords = processedRecords;
  }
  
  public void add(ProcessedRecord rec) {
    this.processedRecords.add(rec);
  }
  
  public int countNewRecord() {
    int count = 0;
    for(ProcessedRecord rec : processedRecords) {
      if(rec.isNewRecord()) count++;
    }
    return count;
  }

  public int countValidRecord() {
    int count = 0;
    for(ProcessedRecord rec : processedRecords) {
      if(rec.isValid()) count++;
    }
    return count;
  }
  
  public int countImported() {
    return countImportStatus(BaseProcessedRecord.ImportStatus.IMPORTED);
  }

  public int countImportStatus(BaseProcessedRecord.ImportStatus status) {
    int count = 0;
    for(ProcessedRecord rec : processedRecords) {
      if(status.equals(rec.getImportStatus())) count++;
    }
    return count;
  }
}
