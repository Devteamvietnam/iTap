package com.devteam.module.company.service.hr.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(
  name = WorkTerm.TABLE_NAME
)
@NoArgsConstructor @Getter @Setter
public class WorkTerm extends WorkTermBase {
  
  final static public String TABLE_NAME = "company_hr_work_term";

  public WorkTerm(String category, String label, String term) {
    super(category, label, term);
  }
}
