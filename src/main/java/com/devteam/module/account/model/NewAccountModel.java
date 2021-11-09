package com.devteam.module.account.model;

import java.util.List;

import com.devteam.module.account.entity.Account;

import com.devteam.module.account.entity.AccountType;
import com.devteam.module.account.entity.UserProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class NewAccountModel {
    String           loginId;
    private Account     account;
    private UserProfile profile;

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

    public NewAccountModel withUserProfile(UserProfile profile) {
        this.profile = profile;
        return this;
    }

    public NewAccountModel withUserProfile(UserProfile profile, String password) {
        withUserProfile(profile);
        account =
                new Account(profile.getLoginId(), password, profile.getEmail(),
                        profile.getMobile(), profile.getFullName(), AccountType.USER)
                        .withFullName(profile.getFullName());
        return this;
    }

}