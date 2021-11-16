package com.devteam.core.security.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.devteam.core.common.ClientInfo;
import com.devteam.core.data.db.entity.ICompanyEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor @Getter @Setter
public class CompanyLoginPermission extends LoginPermission implements ICompanyEntity {
  private static final long serialVersionUID = 1L;

  @NotNull
  @Column(name="company_id")
  private String companyId;
  
  public CompanyLoginPermission(String loginId, String label, Capability cap) {
    super(loginId, label, cap);
    this.type = Type.Employee;
  }
  
  @Override
  public void set(ClientInfo client, String companyId) {
    super.set(client);
    this.companyId = companyId;
  }
}
