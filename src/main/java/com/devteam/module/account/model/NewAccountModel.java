package com.devteam.module.account.model;

import java.util.List;

import com.devteam.module.account.entity.Account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class NewAccountModel {
    String           loginId;
    private Account     account;

    private List<String> accountGroupPaths;

    public NewAccountModel(String loginId) {
        this.loginId = loginId;
    }

    public NewAccountModel(Account account) {
        this.loginId = account.getLoginId();
        this.account = account;
    }

    public NewAccountModel withAccount(Account account) {
        this.account = account;
        return this;
    }

}