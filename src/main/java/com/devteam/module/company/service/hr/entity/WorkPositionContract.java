package com.devteam.module.company.service.hr.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.util.ds.Arrays;
import com.devteam.core.util.ds.Collections;
import com.devteam.core.util.text.DateUtil;
import com.devteam.module.company.core.entity.Company;
import com.devteam.module.company.core.entity.CompanyEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
  name = WorkPositionContract.TABLE_NAME,
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"company_id", "code"}),
  },
  indexes = { 
    @Index(columnList="code,salary_type"),
  }
)
@NoArgsConstructor @Getter @Setter
public class WorkPositionContract extends CompanyEntity {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "company_hr_work_position_contract";

  public static enum ContractType { FIXED_TERM, NON_FIXED_TERM, FREELANCE, APPRENTICESHIP, PROBATIONARY }
  public static enum SalaryType   { Annual, Monthly, Hourly }
  
  @NotNull
  private String label;
  
  @NotNull
  private String code;

  @Enumerated(EnumType.STRING)
  private ContractType type;
  
  @Column(name = "employee_login_id")
  private String employeeLoginId;
  
  @Enumerated(EnumType.STRING)
  private ContractStatus status = ContractStatus.ACTIVE;
  
  @NotNull
  @Column(name = "employee_work_contract_code")
  private String employeeWorkContractCode;

  @Column(name = "work_position_code")
  private String workPositionCode;

  @Column(name = "salary_type")
  @Enumerated(EnumType.STRING)
  private SalaryType salaryType = SalaryType.Monthly;
  
  private double salary;

  @Column(name = "social_insurance_salary")
  private double socialInsuranceSalary;
  
  @Column(name = "annual_take_off")
  private double annualTakeOff;   //in day
  
  @Column(name = "annual_sick_leave")
  private double annualSickLeave; //in day

  @JsonFormat(pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  @Column(name = "valid_from")
  private Date validFrom;
  @JsonFormat(pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  @Column(name = "valid_to")
  private Date validTo;

  @Column(name = "report_to")
  private String reportTo;
  
  @Column(name = "report_to_label")
  private String reportToLabel;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "work_position_contract_id", referencedColumnName = "id")
  private List<EmployeeAllowance> allowances = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "work_position_contract_id", referencedColumnName = "id")
  private List<EmployeeBenefitContribution> benefitContributions = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "work_position_contract_id", referencedColumnName = "id")
  private List<EmployeeWorkContractTerm> terms = new ArrayList<>();

  @Column(name = "bank_account_info")
  private String bankAccountInfo;
  
  @Column(name = "bank_account_owner")
  private String bankAccountOwner;
  
  @Column(name = "bank_account_name")
  private String bankAccountName;
  
  @Column(name = "bank_account_number")
  private String bankAccountNumber;
  
  @Column(name = "bank_address")
  private String bankAdress;

  public WorkPositionContract(WorkPosition workPosition) {
    this.workPositionCode = workPosition.getCode();
    this.salary = workPosition.getBasicSalary();
    Collections.apply(workPosition.getAllowances(), allowance -> {
      withEmployeeContractAllowance(allowance);
    });
    Collections.apply(workPosition.getTerms(), term -> {
      withEmployeeContractTerm(new EmployeeWorkContractTerm(term));
    });
  }

  public WorkPositionContract(String label, String code, Employee employee, WorkPosition workPosition) {
    this(workPosition);
    this.label = label;
    this.code = code;
    this.employeeLoginId = employee.getLoginId();
  }
  
  public WorkPositionContract withEmployeeWorkContract(EmployeeWorkContract contract) {
    this.employeeWorkContractCode = contract.getCode();
    return this;
  }
  
  public WorkPositionContract withLabel(String label) {
    this.label = label;
    return this;
  }

  public WorkPositionContract withSalary(double salary) {
    this.salary = salary;
    return this;
  }
  
  public WorkPositionContract withEmployeeLoginId(String employeeLoginId) {
    this.employeeLoginId = employeeLoginId;
    return this;
  }
  
  public WorkPositionContract withValidFrom(Date validFrom) {
    this.validFrom = validFrom;
    return this;
  }
  
  public WorkPositionContract withValidTo(Date validTo) {
    this.validTo = validTo;
    return this;
  }
  
  public WorkPositionContract withBankAccountName(String bankAccountName) {
    this.bankAccountName = bankAccountName;
    return this;
  }
  
  public WorkPositionContract withBankAccountNumber(String bankAccountNumber) {
    this.bankAccountNumber = bankAccountNumber;
    return this;
  }

  @Deprecated
  public WorkPositionContract withBankAccountInfo(String bankAccountInfo) {
    this.bankAccountInfo = bankAccountInfo;
    return this;
  }

  public WorkPositionContract withEmployeeContractAllowance(WorkAllowance allowance) {
    if(allowances == null) allowances = new ArrayList<>();
    EmployeeAllowance employeeAllowance = new EmployeeAllowance();
    employeeAllowance.copyFromWorkAllowance(allowance);
    allowances.add(employeeAllowance);
    return this;
    }
  
  public WorkPositionContract withSocialInsuranceSalary(double socialInsuranceSalary) {
    this.socialInsuranceSalary = socialInsuranceSalary;
    return this;
  }
  
  public WorkPositionContract withEmployeeContractTerm(EmployeeWorkContractTerm ... contractTerms) {
    terms = Arrays.addToList(terms, contractTerms);
    return this;
  }

  public WorkPositionContract withBenefitContribution(EmployeeBenefitContribution ... contribution) {
    benefitContributions = Arrays.addToList(benefitContributions, contribution);
    return this;
  }

  public WorkPositionContract withReportTo(Employee employee) {
    this.reportTo = employee.getLoginId();
    this.reportToLabel = employee.getLabel();
    return this;
  }
  
  public WorkPositionContract withAnnualTakeOff(double annualTakeOff) {
    this.annualTakeOff = annualTakeOff;
    return this;
  }
  
  public WorkPositionContract withAnnualSickLeave(double annualSickLeave) {
    this.annualSickLeave = annualSickLeave;
    return this;
  }

  public void set(ClientInfo client, Company company) {
    super.set(client, company);
    set(client, company, allowances);
    set(client, company, terms);
    set(client, company, benefitContributions);
  }
  
  @Override
  public String identify() {
    return this.code;
  }
}
