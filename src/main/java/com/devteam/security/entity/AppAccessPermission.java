package com.devteam.security.entity;

import com.devteam.core.enums.AccessType;
import com.devteam.core.enums.Capability;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor @Getter @Setter
public class AppAccessPermission  {
  private String       id;
  private String     appModule;
  private String     appName;
  private String     loginId;
  @Enumerated(EnumType.STRING)
  private AccessType accessType = AccessType.ACCOUNT;
  @Enumerated(EnumType.STRING)
  private Capability capability;
}
