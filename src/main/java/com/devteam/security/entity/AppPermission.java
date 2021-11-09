package com.devteam.security.entity;

import com.devteam.core.entity.BaseEntity;
import com.devteam.core.enums.AccessType;
import com.devteam.core.enums.Capability;
import com.devteam.module.common.ClientInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@JsonInclude(Include.NON_NULL)
@Table(
  name = AppPermission.TABLE_NAME,
  indexes = {
    @Index(columnList = "login_id")
  }
)
@NoArgsConstructor @Getter @Setter
public class AppPermission extends BaseEntity<String> {
  public static final String TABLE_NAME = "security_app_permission";

  @ManyToOne(optional = false)
  @JoinColumn(name = "app_id")
  private App  app;

  @NotNull
  @Column(name = "login_id")
  private String     loginId;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "access_type")
  private AccessType accessType = AccessType.ACCOUNT;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Capability capability;

  public AppPermission(String loginId) {
    this.loginId = loginId;
  }

    public AppPermission withCapability(Capability capability) {
    this.capability = capability;
    return this;
  }

  public AppPermission withApp(App app) {
    this.app = app;
    return this;
  }

  public AppPermission withType(AccessType type) {
    this.accessType = type;
    return this;
  }

  @Override
  public void set(ClientInfo client) {
    super.set(client);
  }
}
