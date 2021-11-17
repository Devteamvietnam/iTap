package com.devteam.module.data.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.devteam.module.data.io.entity.ImportEntry;
import com.devteam.module.data.io.entity.ProcessedRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class ImportEntryProcessedResult {
  
  private ImportEntry entry;
  private Map<String, GroupProcessedRecords> groupProcessedRecords;
 
  public ImportEntryProcessedResult(ImportEntry entry) {
    this.entry     = entry;
    this.groupProcessedRecords = new HashMap<>();
  }
 
  public ImportEntryProcessedResult addAll(List<ProcessedRecord> records) {
    if(groupProcessedRecords == null) groupProcessedRecords = new HashMap<>();
    for(ProcessedRecord rec : records) {
      GroupProcessedRecords group = groupProcessedRecords.get(rec.getGroupName());
      if(group == null) {
        group = new GroupProcessedRecords(rec.getGroupName());
        groupProcessedRecords.put(group.getName(), group);
      }
      group.add(rec);
    }
    return this;
  }
  
  public GroupProcessedRecords getGroupProcessedRecords(String name) {
    return groupProcessedRecords.get(name);
  }
}
