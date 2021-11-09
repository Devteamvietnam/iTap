package com.devteam.security.entity;

import com.devteam.core.entity.BaseEntity;
import com.devteam.core.enums.Capability;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@JsonInclude(Include.NON_NULL)
@Table(
  name = App.TABLE_NAME,
  indexes = {
    @Index(columnList = "name")
  }
)
@NoArgsConstructor @Getter @Setter
public class App extends BaseEntity<String> {
  private static final long serialVersionUID = 1L;

  public static final String TABLE_NAME = "security_app";

  @NotNull
  private String     module;

  @NotNull
  private String     name;

  @Column(name="required_capability")
  private Capability requiredCapability;

  private String     label;
  private String     description;

  public App(@NotNull String module, @NotNull String name) {
    this.module = module;
    this.name = name;
    this.label = name;
  }

  public App withRequiredCapability(Capability capability) {
    this.requiredCapability = capability;
    return this;
  }
}