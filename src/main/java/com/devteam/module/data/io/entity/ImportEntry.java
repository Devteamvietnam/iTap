package com.devteam.module.data.io.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.devteam.core.module.http.upload.UploadResource;
import com.devteam.core.util.dataformat.DataSerializer;
import com.devteam.core.util.text.DateUtil;
import com.devteam.core.util.text.StringUtil;
import com.devteam.module.company.core.entity.CompanyEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
  name = ImportEntry.TABLE_NAME,
  uniqueConstraints = {
    @UniqueConstraint(
      name = ImportEntry.TABLE_NAME + "_code",
      columnNames = {"company_id", "code"}),
  },
  indexes = {
    @Index(columnList="code, module, plugin"),
  }
)
@NoArgsConstructor @Getter @Setter
public class ImportEntry extends CompanyEntity {
  public static final String TABLE_NAME = "data_io_import_entry";

  @NotNull
  private String    code;
  private String    module;
  private String    plugin;
  
  @Column(name = "file_name")
  private String    fileName;
  
  @Column(name = "data_type")
  private String    dataType;
  private long      size;

  @Column(name = "upload_count")
  private int       uploadCount;
  
  @Column(name = "import_count")
  private int       importCount;
  
  @Column(name = "processed_status")
  private String    processedStatus = "NONE";
  
  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  @Column(name = "processed_at")
  private Date      processedAt;
  
  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  @Column(name = "processed_end")
  private Date      processedEnd;
  
  @Column(name = "execution_time")
  private long      executionTime;
  
  @Column(name = "processed_all_count")
  private int       processedAllCount;
  
  @Column(name = "processed_valid_count")
  private int       processedValidCount;
  
  @Column(name = "processed_new_count")
  private int       processedNewCount;
  
  @Transient
  private ImportEntryExtra importEntryExtra = new ImportEntryExtra();  
  
  public ImportEntry withUploadResource(String module, String plugin, UploadResource res) {
    this.code   = module + "-" + plugin + "-" + res.getName();
    this.module = module;
    this.plugin = plugin;
    this.fileName = res.getName();
    this.size     = res.getSize();
    this.dataType = res.getContentType();
    this.uploadCount++;
    return this;
  }
  
  @JsonIgnore
  @Access(AccessType.PROPERTY)
  @Column(length =  32 * 1024, name = "import_entry_extra_json")
  public String getImportEntryExtraJson() {
    if(this.importEntryExtra == null) return null;
    String json = DataSerializer.JSON.toString(this.importEntryExtra);
    return json;
  }

  public void setImportEntryExtraJson(String json) {
    if(StringUtil.isEmpty(json)) {
      this.importEntryExtra= null;
    } else {
      this.importEntryExtra = DataSerializer.JSON.fromString(json, ImportEntryExtra.class);
    }
  }
  
  public ImportEntry incrUploadCount(int count) {
    uploadCount +=  count;
    return this;
  }

  public ImportEntry incrImportCount(int count) {
    importCount +=  count;
    return this;
  }

  public <T> ImportEntry withError(Throwable error) {
    importEntryExtra.withError(error);
    return this;
  }

  public void addProcessedLog(List<Log> logs) {
    importEntryExtra.addProcessedLog(logs);
  }
  
  public void clearProcessedInfo() {
    importEntryExtra = new ImportEntryExtra();
    processedAllCount = 0;
    processedValidCount = 0;
    processedNewCount = 0;
  }

}