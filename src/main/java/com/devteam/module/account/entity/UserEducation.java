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
    name = UserEducation.TABLE_NAME,
    indexes = {
      @Index(columnList="login_id"),
    }
 )
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter @Setter
public class UserEducation extends PersistableEntity<Long> {
  public static final String TABLE_NAME = "account_user_education";
  
  @NotNull
  @Column(name = "login_id")
  private String loginId;
  
  @NotNull
  private String label;
  
  @Column(name = "school_or_institute_name")
  private String schoolOrInstituteName;
  
  @Column(name = "school_or_institute_label")
  private String schoolOrInstituteLabel;
  
  @JsonFormat(pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  @Column(name = "from_date")
  private Date   fromDate;
  
  @JsonFormat(pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  @Column(name = "to_date")
  private Date   toDate;
  
  @Column(name = "major_name")
  private String majorName;
  
  @Column(name = "major_label")
  private String majorLabel;
  
  private String certificate;

  @Column(name = "language_name")
  private String languageName;

  @Column(name = "language_label")
  private String languageLabel;
  
  private String note;
  
  public UserEducation(String loginId) {
    this.loginId = loginId;
  }
  
  public UserEducation withLoginId(String loginId) {
    this.loginId = loginId;
    return this;
  }
  
  public UserEducation withLabel(String val) {
    label = val;
    return this;
  }
  
  public UserEducation withSchoolOrInstitute(String val) {
    if(label == null) label = val;
    schoolOrInstituteName = val;
    schoolOrInstituteLabel = val;
    return this;
  }
  
  public UserEducation withFrom(Date val) {
    fromDate = val;
    return this;
  }
  
  public UserEducation withTo(Date val) {
    toDate = val;
    return this;
  }
  
  public UserEducation withMajor(String val) {
    majorName = val;
    majorLabel = val;
    return this;
  }
  
  public UserEducation withCertificate(String val) {
    certificate = val;
    return this;
  }
  
  public UserEducation withLanguage(String val) {
    languageName = val;
    languageLabel = val;
    return this;
  }
  
  public UserEducation withNote(String val) {
    note = val;
    return this;
  }
}