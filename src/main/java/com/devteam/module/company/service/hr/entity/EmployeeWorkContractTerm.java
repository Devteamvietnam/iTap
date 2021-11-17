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
    name = EmployeeWorkContractTerm.TABLE_NAME
)
@NoArgsConstructor @Getter @Setter
public class EmployeeWorkContractTerm extends WorkContractTermBase {

  final static public String TABLE_NAME = "company_hr_employee_work_contract_term";

  @ManyToOne(optional = true)
  @JoinColumn(name = "workContractTermId", referencedColumnName = "id")
  private WorkContractTerm workContractTerm;

  public EmployeeWorkContractTerm(WorkContractTerm term) {
    super(term.getCategory(), term.getLabel(), term.getTerm());
    this.workContractTerm = term;
  }

  public EmployeeWorkContractTerm withWorkContractTerm(WorkContractTerm term) {
    workContractTerm = term;
    return this;
  }
}