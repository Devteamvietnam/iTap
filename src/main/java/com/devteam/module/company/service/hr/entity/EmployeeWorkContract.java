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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.util.ds.Arrays;
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
  name = EmployeeWorkContract.TABLE_NAME,
  uniqueConstraints = {
    @UniqueConstraint(
        columnNames = {"company_id", "code"}),
  },
  indexes = {
    @Index(columnList="code"),
  }
)
@Getter @Setter @NoArgsConstructor
public class EmployeeWorkContract extends CompanyEntity {
  private static final long serialVersionUID = 1L;

  final static public String TABLE_NAME = "company_hr_employee_work_contract";

  private String   label;

  @NotNull
  private String   code;
  
  @Column(name = "employee_tax_code")
  private String   employeeTaxCode;

  @Enumerated(EnumType.STRING)
  private ContractStatus status = ContractStatus.DRAFT;
  
  @Column(name = "employee_login_id")
  private String   employeeLoginId;
  
  @Column(name = "employee_label")
  private String   employeeLabel;
  
  @OneToOne(optional = false)
  @JoinColumn(name = "work_position_contract_id")
  private WorkPositionContract workPositionContract;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "employee_work_contract_id", referencedColumnName = "id")
  private List<EmployeeWorkTerm> terms = new ArrayList<>();

  @JsonFormat(pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  @Column(name = "start_date")
  private Date startDate;

  @JsonFormat(pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  @Column(name = "end_date")
  private Date endDate;

  @Column(name = "hire_by")
  private String   hireBy;
  
  @Column(name = "hire_by_label")
  private String   hireByLabel;

  public EmployeeWorkContract(Employee employee) {
    this.employeeLoginId = employee.getLoginId();
    this.employeeLabel = employee.getLabel();
    this.label = employee.getLoginId() + " Contract";
    this.code = employee.getLoginId() + "-contract";
  }
  
  public EmployeeWorkContract(String label, String code, Employee employee) {
    this.employeeLoginId = employee.getLoginId();
    this.employeeLabel = employee.getLabel();
    this.label = label;
    this.code = code;
  }

  public EmployeeWorkContract withSatatus(ContractStatus status) {
    this.setStatus(status);
    return this;
  }
  
  public EmployeeWorkContract withStartDate(Date startDate) {
    this.startDate = startDate;
    return this;
  }
  
  public EmployeeWorkContract withEndDate(Date endDate) {
    this.endDate = endDate;
    return this;
  }
  
  public EmployeeWorkContract withHireBy(String hireBy) {
    this.hireBy = hireBy;
    return this;
  }
  
  public EmployeeWorkContract withHireByLabel(String hireByLabel) {
    this.hireByLabel = hireByLabel;
    return this;
  }
  
  public EmployeeWorkContract withTaxCode(String taxCode) {
    this.employeeTaxCode = taxCode;
    return this;
  }

  public EmployeeWorkContract withWorkTerm(EmployeeWorkTerm ... employeeWorkTerms) {
    terms = Arrays.addToList(terms, employeeWorkTerms);
    return this;
  }
  
  public EmployeeWorkContract withWorkPositionContract(WorkPositionContract workPositionContract) {
    this.workPositionContract = workPositionContract;
    return this;
  }

  @Override
  public void set(ClientInfo client, Company company) {
    super.set(client, company);
    set(client, company, terms);
  }

  public String identify() { return code; }
 }
