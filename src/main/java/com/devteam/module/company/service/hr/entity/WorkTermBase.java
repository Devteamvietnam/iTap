package com.devteam.module.company.service.hr.entity;

import javax.persistence.MappedSuperclass;

import com.devteam.module.company.core.entity.ShareableCompanyEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@MappedSuperclass
@NoArgsConstructor @Getter @Setter
public class WorkTermBase extends ShareableCompanyEntity {
  private String category;
  private String label;
  private String term;
  
  public WorkTermBase(String category, String label, String term) {
    this.category = category;
    this.label = label;
    this.term = term;
  }
  
  public String identify() { return term; }
}
