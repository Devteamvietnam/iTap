package com.devteam.module.company.service.hr.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(
  name = WorkAllowance.TABLE_NAME,
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"company_id", "code"}),
  },
  indexes = {
    @Index(columnList="code"),
  }
)
@Getter
@Setter
@NoArgsConstructor
public class WorkAllowance extends AbstractAllowance {
  private static final long serialVersionUID = 1L;

  final static public String TABLE_NAME = "company_hr_work_allowance";
  
  public WorkAllowance(String label, AllowanceMethod type) {
    this.label = label;
    this.code = label.toLowerCase().replace(" ", "-");
    this.allowanceMethod = type;
  }
  
  public WorkAllowance(String code) {
    this.code = code;
  }
  
  public WorkAllowance withLabel(String label) {
    this.label = label;
    return this;
  }

  public WorkAllowance withDescription(String description) {
    this.description = description;
    return this;
  }

  public WorkAllowance withAmount(double amount) {
    this.amount = amount;
    return this;
  }
  
  public WorkAllowance withPercentage(double percentage) {
    this.salaryPercentage = percentage;
    return this;
  }

  
  public String identify() { return code; }
}
