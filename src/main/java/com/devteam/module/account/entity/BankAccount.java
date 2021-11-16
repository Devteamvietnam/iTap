package com.devteam.module.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.devteam.core.module.data.db.entity.PersistableEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
  name = BankAccount.TABLE_NAME,
  indexes = {
    @Index(columnList="login_id"),
})
@JsonInclude(Include.NON_NULL)
@Getter @Setter
public class BankAccount extends PersistableEntity<Long> {
  public static final String TABLE_NAME = "account_bank_account";

  @Column(name = "login_id")
  private String loginId;
  
  @NotNull
  @Column(name = "account_holder")
  private String accountHolder;
  
  @Column(name = "account_number")
  private String accountNumber;
  
  @Column(name = "bank_name")
  private String bankName;
  
  @Column(name = "bank_address")
  private String bankAdress;
  
  public BankAccount withLoginId(String loginId) {
    this.loginId = loginId;
    return this;
  }
  
  public BankAccount withAccountHolder(String accountHolder) {
    this.accountHolder =accountHolder;
    return this;
  }
  
  public BankAccount withAccountNumber(String accountNumber) {
    this.accountNumber =accountNumber;
    return this;
  }
}
