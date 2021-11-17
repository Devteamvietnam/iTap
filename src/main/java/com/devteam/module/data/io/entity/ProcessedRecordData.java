package com.devteam.module.data.io.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.devteam.core.util.dataformat.DataSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class ProcessedRecordData {
  private String              entityType;
  private String              entityJson;
  private Map<String, Object> rawData;
  private List<Log>           logs;

  public <T> ProcessedRecordData withEntity(T entity) {
    this.entityType = entity.getClass().getName();
    this.entityJson = DataSerializer.JSON.toString(entity);
    return this;
  }
 
  public ProcessedRecordData withRawData(Map<String, Object> rawData) {
    this.rawData = rawData;
    return this;
  }
  
  public void addInfo(String log) {
    addLog(Log.Level.Info, log);
  }

  public void addWarn(String log) {
    addLog(Log.Level.Warn, log);
  }

  public void addError(String log) {
    addLog(Log.Level.Error, log);
  }

  public void addLog(Log.Level level, String log) {
    if(logs == null) logs = new ArrayList<>();
    logs.add(new Log(level, log));
  }
}
