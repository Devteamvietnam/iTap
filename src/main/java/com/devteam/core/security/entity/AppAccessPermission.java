package com.devteam.core.security.entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class AppAccessPermission  {
  private String       id;
  private String     appModule;
  private String     appName;
  private String       companyId;
  private String     loginId;
  private String     appRole;
  private String     appRoleLabel;
  @Enumerated(EnumType.STRING)
  private AccessType accessType = AccessType.Employee;
  @Enumerated(EnumType.STRING)
  private Capability capability;
}
