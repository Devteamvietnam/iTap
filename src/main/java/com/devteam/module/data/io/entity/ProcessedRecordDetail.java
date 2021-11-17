package com.devteam.module.data.io.entity;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.devteam.core.util.dataformat.DataSerializer;
import com.devteam.core.util.text.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
  name = ProcessedRecordDetail.TABLE_NAME,
  indexes = {
    @Index(
      columnList="group_name, label")
  }
)
@NoArgsConstructor @Getter @Setter
public class ProcessedRecordDetail extends BaseProcessedRecord {
  public static final String TABLE_NAME = "data_io_processed_record";

  @Transient
  private ProcessedRecordData processedRecordData;
  
  public ProcessedRecordData getProcessedRecordData() {
    if(processedRecordData == null) processedRecordData = new ProcessedRecordData();
    return processedRecordData;
  }
  
  @JsonIgnore
  @Access(AccessType.PROPERTY)
  @Column(name="processed_record_data_json", length =  32 * 1024)
  public String getProcessedRecordDataJson() {
    if(this.processedRecordData == null) return null;
    String json = DataSerializer.JSON.toString(this.processedRecordData);
    return json;
  }

  public void setProcessedRecordDataJson(String json) {
    if(StringUtil.isEmpty(json)) {
      this.processedRecordData= null;
    } else {
      this.processedRecordData = DataSerializer.JSON.fromString(json, ProcessedRecordData.class);
    }
  }
  
  public <T> ProcessedRecordDetail withEntity(T entity) {
    getProcessedRecordData().withEntity(entity);
    return this;
  }
 
  public ProcessedRecordDetail withRawData(Map<String, Object> rawData) {
    getProcessedRecordData().withRawData(rawData);
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
    getProcessedRecordData().addLog(level, log);
  }
 
  public void dumpRawData() {
    System.out.println(DataSerializer.JSON.toString(getProcessedRecordData().getRawData()));
  }

  public void dumpEntity() {
    System.out.println(getProcessedRecordData().getEntityJson());
  }
}
