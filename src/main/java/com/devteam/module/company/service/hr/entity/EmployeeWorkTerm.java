package com.devteam.module.company.service.hr.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
  name = EmployeeWorkTerm.TABLE_NAME
)
@NoArgsConstructor @Getter @Setter
public class EmployeeWorkTerm extends WorkTermBase {
  
  final static public String TABLE_NAME = "company_hr_employee_work_term";
  
  @ManyToOne(optional = true)
  @JoinColumn(name = "work_term_id", referencedColumnName = "id")
  private WorkTerm workTerm;

  public EmployeeWorkTerm(WorkTerm term) {
    super(term.getCategory(), term.getLabel(), term.getTerm());
    this.workTerm = term;
  }
  
  public EmployeeWorkTerm withWorkContractTerm(WorkTerm term) {
    workTerm = term;
    return this;
  }
}