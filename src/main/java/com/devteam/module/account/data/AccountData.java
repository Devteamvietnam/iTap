package com.devteam.module.account.data;

import com.devteam.core.sample.PersistableEntityAssert;
import com.devteam.core.sample.SampleData;
import com.devteam.module.account.entity.Account;
import com.devteam.module.account.entity.UserProfile;
import com.devteam.module.account.service.AccountService;
import com.devteam.module.common.ClientInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AccountData extends SampleData {
    @Autowired
    protected AccountService accountService;

    public class AccountAssert extends PersistableEntityAssert<Account> {
        public AccountAssert(ClientInfo client, Account account) {
            super(client, account);
            this.methods = new EntityServiceMethods() {
                public Account load() {
                    return accountService.getAccount(client, account.getLoginId());
                }

                public Account save(Account ac) {
                    accountService.saveAccount(client, ac);
                    return load();
                }

                public List<?> searchEntity() {
                    return accountService.searchAccounts(client, createSearchQuery(entity.getLoginId()));
                }

                public boolean archive() {
                    return accountService.changeAccountStorageState(client, createArchivedStorageRequest(entity));
                }
            };
        }
    }

    public class UserProfileAssert extends PersistableEntityAssert<UserProfile> {
        public UserProfileAssert(ClientInfo client, UserProfile profile) {
            super(client, profile);
            this.methods = new EntityServiceMethods() {

                public UserProfile save(UserProfile clone) {
                    return accountService.saveUserProfile(client, clone);
                }
            };
        }
    }

}
