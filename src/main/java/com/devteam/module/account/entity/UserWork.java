package com.devteam.module.account.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.devteam.core.module.data.db.entity.PersistableEntity;
import com.devteam.core.util.text.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
  name = UserWork.TABLE_NAME,
  indexes = {
    @Index(columnList="login_id"),
  }
)
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor @Getter @Setter
public class UserWork extends PersistableEntity<Long> {
  public static final String TABLE_NAME = "account_user_work";

  @NotNull
  @Column(name = "login_id")
  private String loginId;
  
  @NotNull
  private String label;
  
  @Column(name = "organization_label")
  private String organizationLabel;
  
  @Column(name = "organization_name")
  private String organizationName;
  
  @JsonFormat(pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  @Column(name = "from_date")
  private Date   fromDate;
  
  @JsonFormat(pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  @Column(name = "to_date")
  private Date   toDate;
  
  @Column(name = "position_label")
  private String positionLabel;
  
  @Column(name = "position_name")
  private String positionName;
  
  private String description;
  
  public UserWork withLoginId(String loginId) {
    this.loginId = loginId;
    return this;
  }
  
  public UserWork withLabel(String label) {
    this.label = label;
    return this;
  }
  
  public UserWork withOrganization(String val) {
    if(label == null) label = val;
    organizationName = val;
    organizationLabel = val;
    return this;
  }
  
  public UserWork withFrom(Date val) {
    fromDate = val;
    return this;
  }
  
  public UserWork withTo(Date val) {
    toDate = val;
    return this;
  }
  public UserWork withPosition(String val) {
    positionName = val;
    
    return this;
  }
  
  public UserWork withDescription(String val) {
    description = val;
    return this;
  }
}