package com.devteam.core.module.misc.print;

import java.util.Date;

import com.devteam.core.util.text.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
public class PrintJob {
  @Getter @Setter
  private String name;

  @Getter @Setter
  private String description;
 
  @Getter @Setter
  private String queueName;
  
  @Getter @Setter
  private int    copies;
    
  @Getter @Setter
  private String contentType;
  
  @Getter @Setter
  private byte[] data;
  
  @JsonFormat(pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  @Getter @Setter
  private Date timestamp;
  
  public PrintJob() { }
  
  public PrintJob(String name, String description, String queueName, String contentType, byte[] data, int copies) {
    this.name        = name;
    this.description = description;
    this.queueName   = queueName;
    this.contentType = contentType;
    this.data        = data;
    this.copies      = copies;
    this.timestamp   = new Date();
  }
  
  public PrintJob cloneWithoutData() {
    return new PrintJob(name, description, queueName, contentType, null, -1);
  }
}
