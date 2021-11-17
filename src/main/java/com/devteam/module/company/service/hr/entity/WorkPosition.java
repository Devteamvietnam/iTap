package com.devteam.module.company.service.hr.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.devteam.module.company.core.entity.CompanyEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(
  name = WorkPosition.TABLE_NAME,
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
public class WorkPosition extends CompanyEntity {
  private static final long serialVersionUID = 1L;
  
  final static public String TABLE_NAME = "company_hr_work_position";

  @NotNull
  private String code;
  private String label;
  private String description;
  @Column(name="basic_salary")
  private double basicSalary;
  @Column(name="performance_salary")
  private double performanceSalary;

  @Column(name="hr_department_id")
  private Long hrDepartmentId;

  @ManyToMany(cascade = { CascadeType.MERGE })
  @JoinTable(
    name = "company_hr_work_position_allowance_rel",
    joinColumns = @JoinColumn(name = "workPositionId"), 
    inverseJoinColumns = @JoinColumn(name = "workPositionAllowanceId")
  )
  private List<WorkAllowance> allowances;

  @ManyToMany(cascade = { CascadeType.MERGE })
  @JoinTable(
    name = "company_hr_work_position_contract_term_rel",
    joinColumns = @JoinColumn(name = "workPositionId"), 
    inverseJoinColumns = @JoinColumn(name = "workPositionContractTermId")
  )
  private List<WorkContractTerm> terms;
  
  /*
  @Transient
  private HRDepartment   department;
  */
  public WorkPosition(String code) {
    this.code = code;
  }

  public WorkPosition withLabel(String label) {
    this.label = label;
    return this;
  }

  public WorkPosition withDescription(String description) {
    this.description = description;
    return this;
  }

  public WorkPosition withBasicSalary(double basicSalary) {
    this.basicSalary = basicSalary;
    return this;
  }

  public WorkPosition withPerformanceSalary(double performanceSalary) {
    this.performanceSalary = performanceSalary;
    return this;
  }

  public WorkPosition withWorkAllowance(WorkAllowance allowance) {
    if(allowances == null) allowances = new ArrayList<>();
    allowances.add(allowance);
    return this;
  }

  public WorkPosition withHRDepartment(HRDepartment dept) {
    //this.department = dept;
    if(dept != null) {
      this.hrDepartmentId = dept.getId();
    }
    return this;
  }
  
  public WorkPosition withWorkContractTerm(WorkContractTerm workContractTerm) {
    if (terms == null) terms = new ArrayList<>();
    terms.add(workContractTerm);
    return this;
  }
  
  public String identify() { return code; }
}
