package com.devteam.module.data.io.entity;


import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.devteam.module.company.core.entity.CompanyEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@NoArgsConstructor @Getter @Setter
public class BaseProcessedRecord extends CompanyEntity {
  public static enum ImportStatus { NOT_IMPORT, IMPORTED, IMPORT_ERROR } ;
  
  @NotNull
  @Column(name="entry_id")
  private Long entryId;
  @Column(name="group_name")
  private String groupName;
  
  private String              label;
  private boolean             valid     = false;
  @Column(name="new_record")
  private boolean             newRecord = true;
  @Enumerated(EnumType.STRING)
  @Column(name="import_status")
  private ImportStatus        importStatus = ImportStatus.NOT_IMPORT;
}
