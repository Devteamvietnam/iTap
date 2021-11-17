package com.devteam.module.company.service.hr.entity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.devteam.module.company.core.entity.CompanyEntity;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
public class AbstractAllowance extends CompanyEntity {
  public static enum AllowanceMethod { FIXED_AMOUNT, WORKING_DAY, FULL_SALARY, INSURANCE_SALARY }
  
  @NotNull
  protected String code; 
  protected String label;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "allowance_method")
  protected AllowanceMethod allowanceMethod;
  
  @Column(name = "salary_percentage")
  protected double salaryPercentage;
  protected String unit;
  protected double amount;
  protected String currency;
  protected String description;
}
