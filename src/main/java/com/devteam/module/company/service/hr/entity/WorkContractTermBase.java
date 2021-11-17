package com.devteam.module.company.service.hr.entity;

import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@NoArgsConstructor @Getter @Setter
public class WorkContractTermBase extends WorkTermBase {
  public WorkContractTermBase(String category, String label, String term) {
    super(category, label, term);
  }
}
