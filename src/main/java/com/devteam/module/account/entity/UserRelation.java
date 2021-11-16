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
  name = UserRelation.TABLE_NAME,
  indexes = {
    @Index(columnList="login_id"),
  }
)
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor @Getter @Setter
public class UserRelation extends PersistableEntity<Long> {
  private static final long serialVersionUID = 1L;

  public static final String TABLE_NAME = "account_user_relation";

  @NotNull
  @Column(name = "login_id")
  private String loginId;
  
  @NotNull
  private String label;
  
  @Column(name = "relation_name")
  private String relationName;
  
  @Column(name = "relation_label")
  private String relationLabel;
  
  private String gender = "Male";
  
  @Column(name = "first_name")
  private String firstName;
  
  @Column(name = "last_name")
  private  String lastName;

  @Column(name = "full_name")
  private  String fullName;
  
  @JsonFormat(pattern = DateUtil.COMPACT_DATETIME_FORMAT)
  private  Date   birthday;
  
  @Column(name = "occupation_name")
  private  String occupationName;
  
  @Column(name = "occupation_label")
  private  String occupationLabel;
  
  @Column(length=1024)
  private String contact;
  
  @Column(length=4 * 1024)
  private String note;
  
  public UserRelation withLoginId(String loginId) {
    this.loginId = loginId;
    return this;
  }
  
  public UserRelation withLabel(String val) {
    label = val;
    return this;
  }
  
  public UserRelation withRelation(String val) {
    if(label == null) label = val;
    relationName = val;
    return this;
  }
  
  public UserRelation withGender(String val) {
    gender = val;
    return this;
  }
  
  public UserRelation withFirstName(String val) {
    firstName = val;
    return this;
  }
  
  public UserRelation withLastName(String val) {
    lastName = val;
    return this;
  }
  
  public UserRelation withFullName(String val) {
    fullName = val;
    return this;
  }
  
  public UserRelation withContact(String val) {
    contact = val;
    return this;
  }
  
  public UserRelation withBirthday(Date val) {
    birthday = val;
    return this;
  }
  
  public UserRelation withOccupation(String val) {
    occupationName = val;
    occupationLabel = val;
    return this;
  }
  
  public UserRelation withNote(String val) {
    note = val;
    return this;
  }
}
