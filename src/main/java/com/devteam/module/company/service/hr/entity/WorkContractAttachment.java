package com.devteam.module.company.service.hr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.devteam.module.storage.entity.CompanyEntityAttachment;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = WorkContractAttachment.TABLE_NAME)
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class WorkContractAttachment extends CompanyEntityAttachment {

  final static public String TABLE_NAME = "company_hr_work_contract_attachment";

  @NotNull
  @Column(name = "work_contract_id")
  private Long workContractId;

  public WorkContractAttachment(String name, String label, String description) {
    super(name, label, description);
  }
}
