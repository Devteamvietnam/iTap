package com.devteam.module.company.service.hr.entity;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.devteam.core.util.bean.BeanUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
  name = EmployeeAllowance.TABLE_NAME,
  indexes = {
    @Index(columnList="code"),
  }
)
@Getter
@Setter
@NoArgsConstructor
public class EmployeeAllowance extends AbstractAllowance {
  private static final long serialVersionUID = 1L;

  final static public String TABLE_NAME = "company_hr_employee_allowance";
  
  public void copyFromWorkAllowance(WorkAllowance allowance) {
    List<Field> fields = BeanUtil.getFields(AbstractAllowance.class);
    BeanUtil.copyAllFields(this, allowance, fields);
    setId(null);
  }
}
