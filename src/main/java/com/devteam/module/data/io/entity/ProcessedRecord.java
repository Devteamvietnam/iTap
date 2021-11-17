package com.devteam.module.data.io.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
  name = ProcessedRecord.TABLE_NAME
)
@NoArgsConstructor @Getter @Setter
public class ProcessedRecord extends BaseProcessedRecord {
  public static final String TABLE_NAME = "data_io_processed_record";
}
