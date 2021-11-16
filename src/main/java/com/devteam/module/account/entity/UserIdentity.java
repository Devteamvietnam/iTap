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
    name = UserIdentity.TABLE_NAME,
    indexes = { 
        @Index(columnList = "login_id")
    }
)
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class UserIdentity extends PersistableEntity<Long> {
  private static final long serialVersionUID = 1L;

  public static final String TABLE_NAME = "account_user_identity";

  @NotNull
  @Column(name = "login_id")
  private String loginId;

  private String name;

  @NotNull
  private String label;

  @Column(name = "identification_no")
  private String identificationNo;

  private String type;

  @JsonFormat(pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  @Column(name = "issue_date")
  private Date issueDate;

  @Column(name = "issue_place_name")
  private String issuePlaceName;

  @Column(name = "issue_place_label")
  private String issuePlaceLabel;
  
  @Column(name = "issue_by_name")
  private String issueByName;
  
  @Column(name = "issue_by_label")
  private String issueByLabel;

  @JsonFormat(pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  @Column(name = "valid_from")
  private Date validFrom;

  @JsonFormat(pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  @Column(name = "valid_to")
  private Date validTo;

  public UserIdentity withLoginId(String loginId) {
    this.loginId = loginId;
    return this;
  }

  public UserIdentity withLabel(String label) {
    this.label = label;
    return this;
  }
  
  public UserIdentity withIssuePlaceLabel(String issuePlaceLabel) {
    this.issuePlaceLabel = issuePlaceLabel;
    return this;
  }
  
  public UserIdentity withValid(Date validFrom, Date validTo) {
    this.validFrom = validFrom;
    this.validTo = validTo;
    return this;
  }

  public UserIdentity withInfo(String label, String name, String identificationNo, String type) {
    this.label = label;
    this.name = name;
    this.identificationNo = identificationNo;
    this.type = type;
    return this;
  }
}