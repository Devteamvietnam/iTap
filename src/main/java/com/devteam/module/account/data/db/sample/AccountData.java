package com.devteam.module.account.data.db.sample;

import java.util.List;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.sample.PersistableEntityAssert;
import com.devteam.core.module.data.db.sample.SampleData;
import com.devteam.module.account.AccountService;
import com.devteam.module.account.entity.*;
import org.springframework.beans.factory.annotation.Autowired;


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

  public class OrgProfileAssert extends PersistableEntityAssert<OrgProfile> {

    public OrgProfileAssert(ClientInfo client, OrgProfile profile) {
      super(client, profile);
      this.methods = new EntityServiceMethods() {
        public OrgProfile save(OrgProfile clone) {
          return accountService.saveOrgProfile(client, clone);
        }
      };
    }

  }
  
  public class BankAssert extends PersistableEntityAssert<BankAccount> {
    public BankAssert(ClientInfo client, String loginId) {
      super(client, null);
      this.methods = new EntityServiceMethods() {
        public List<BankAccount> loadList() {
          return accountService.findBankAccount(client, loginId);
        }

        public List<BankAccount> saveList(List<BankAccount> bank) {
          accountService.saveBankAccounts(client, loginId, bank);
          return loadList();
        }
      };
    }
  }

  public class UserRealationAssert extends PersistableEntityAssert<UserRelation> {
    public UserRealationAssert(ClientInfo client, String loginId) {
      super(client, null);
      this.methods = new EntityServiceMethods() {
        public List<UserRelation> loadList() {
          return accountService.findUserRelation(client, loginId);
        }

        public List<UserRelation> saveList(List<UserRelation> userRelation) {
          accountService.saveUserRelations(client, loginId, userRelation);
          return loadList();
        }
      };
    }
  }

  public class UserWorkAssert extends PersistableEntityAssert<UserWork> {
    public UserWorkAssert(ClientInfo client, String loginId) {
      super(client, null);
      this.methods = new EntityServiceMethods() {
        public List<UserWork> loadList() {
          return accountService.findUserWork(client, loginId);
        }

        public List<UserWork> saveList(List<UserWork> workList) {
          accountService.saveUserWorks(client, loginId, workList);
          return loadList();
        }
      };
    }
  }

  public class UserIdentityAssert extends PersistableEntityAssert<UserIdentity> {
    public UserIdentityAssert(ClientInfo client, String loginId) {
      super(client, null);
      this.methods = new EntityServiceMethods() {
        public List<UserIdentity> loadList() {
          return accountService.findUserIdentity(client, loginId);
        }

        public List<UserIdentity> saveList(List<UserIdentity> identityList) {
          accountService.saveUserIdentitys(client, loginId, identityList);
          return loadList();
        }
      };
    }
  }

  public class UserEducationAssert extends PersistableEntityAssert<UserEducation> {
    public UserEducationAssert(ClientInfo client, String loginId) {
      super(client, null);
      this.methods = new EntityServiceMethods() {
        public List<UserEducation> loadList() {
          return accountService.findUserEducation(client, loginId);
        }

        public List<UserEducation> saveList(List<UserEducation> educationList) {
          accountService.saveUserEducations(client, loginId, educationList);
          return loadList();
        }
      };
    }
  }
}
