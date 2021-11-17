package com.devteam.module.company.service.hr.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
  name = WorkContractTerm.TABLE_NAME
)
@NoArgsConstructor @Getter @Setter
public class WorkContractTerm extends WorkContractTermBase {
  
  final static public String TABLE_NAME = "company_hr_work_contract_term";
  
  public WorkContractTerm(String category, String label, String term) {
    super(category, label, term);
  }
}
