package com.devteam.module.company.service.hr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.devteam.module.company.core.entity.CompanyPersistable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(
  name = EmployeeBenefitContribution.TABLE_NAME,
  indexes = {
    @Index(columnList="category"),
  }
)
@Getter @Setter
@NoArgsConstructor
public class EmployeeBenefitContribution extends CompanyPersistable {
  private static final long serialVersionUID = 1L;

  final static public String TABLE_NAME = "company_hr_employee_benefit_contribution";
  
  public static enum BenefitContributionMethod { FIXED_AMOUNT, FULL_SALARY, INSURANCE_SALARY }
  
  private String category;
  @NotNull
  private String label;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "contribution_method")
  private BenefitContributionMethod contributionMethod;
  
  private double quantity;
  
  @Column(name = "salary_percentage")
  private double salaryPercentage;

  @Column(name = "amount_per_unit")
  private double amountPerUnit;
  
  private double amount;
  private String currency;
  private String description;
  
  public EmployeeBenefitContribution(String category, String label, BenefitContributionMethod method) {
    this.category = category;
    this.label = label;
    this.contributionMethod = method;
  }

  public EmployeeBenefitContribution withPercentage(double percentage) {
    this.salaryPercentage = percentage;
    return this;
  }
}
