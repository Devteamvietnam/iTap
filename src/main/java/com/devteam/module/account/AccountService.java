package com.devteam.module.account;

import java.util.List;

import javax.annotation.PostConstruct;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.common.Result;
import com.devteam.core.module.data.db.entity.ChangeStorageStateRequest;
import com.devteam.core.module.data.db.entity.StorageState;
import com.devteam.core.module.data.db.query.SqlQueryParams;
import com.devteam.core.module.http.upload.UploadResource;
import com.devteam.core.module.security.SecurityLogic;
import com.devteam.core.module.security.entity.AccessToken;
import com.devteam.core.util.error.ErrorType;
import com.devteam.core.util.error.RuntimeError;
import com.devteam.module.account.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;



@Service("AccountService")
public class AccountService {
  @Autowired
  private AccountLogic accountLogic;

  @Autowired
  private AccountGroupLogic groupLogic;

  @Getter
  @Autowired
  private ProfileLogic profileLogic;

  @Autowired
  private UserLogic userLogic;

  @Autowired
  private SecurityLogic securityLogic;

  @PostConstruct
  public void onInit() {
  }

  @Transactional
  public AccessToken authenticate(ClientInfo client, String loginId, String password, int liveTimeInMin) {
    Account account = accountLogic.authenticate(client, loginId, password);
    if(account == null) {
      return new AccessToken("anon", AccessToken.AccessType.None);
    } 
    AccessToken token = 
        new AccessToken(null, AccessToken.AccessType.Employee)
        .withLabel(account.getFullName())
        .withLoginId(loginId)
        .withGenToken()
        .withLiveTime(liveTimeInMin);
    token = securityLogic.saveAccessToken(client, token);
    return token;
  }

  // Account Group
  @Transactional(readOnly = true)
  public AccountGroup getAccountGroup(ClientInfo client, String name) {
    return groupLogic.getAccountGroup(client, name);
  }
  
  @Transactional(readOnly = true)
  public AccountGroup getAccountGroupById(ClientInfo client, Long id) {
    return groupLogic.getAccountGroupById(client, id);
  }

  @Transactional(readOnly = true)
  public List<AccountGroup> findAccountGroupChildren(ClientInfo client, Long groupId) {
    return groupLogic.findChildren(client, groupId);
  }

  @Transactional
  public AccountGroup createAccountGroup(ClientInfo client, AccountGroup parent, AccountGroup group) {
    return groupLogic.createAccountGroup(client, parent, group);
  }

  @Transactional
  public AccountGroup saveAccountGroup(ClientInfo client, AccountGroup group) {
    return groupLogic.saveAccountGroup(client, group);
  }
  
  @Transactional
  public Boolean deleteAccountGroup(ClientInfo client, Long id) {
    return groupLogic.delete(client, id);
  }

  @Transactional
  public AccountMembership createMembership(ClientInfo client, AccountGroup group, String loginId) {
    return accountLogic.createMembership(client, group, loginId);
  }
  
  @Transactional
  public boolean createAccountMemberships(ClientInfo clientInfo, Long groupId, List<String> accountLoginIds) {
    return groupLogic.createAccountMemberships(clientInfo, groupId, accountLoginIds);
  }
  
  @Transactional
  public boolean deleteAccountMemberships(ClientInfo clientInfo, Long groupId, List<String> accountLoginIds) {
    return groupLogic.deleteAccountMemberships(clientInfo, groupId, accountLoginIds);
  }

  @Transactional
  public UploadResource uploadAvatar(ClientInfo client, String loginId, UploadResource resource, boolean saveOrigin) {
    return profileLogic.uploadAvatar(client, loginId, resource, saveOrigin);
  }

  //Account
  @Transactional
  public NewAccountModel createNewAccount(ClientInfo client, NewAccountModel model) {
    return accountLogic.createNewAccount(client, model);
  }

  @Transactional
  public Account saveAccount(ClientInfo client, Account account) {
    if(account.isNew()) {
      throw new RuntimeError(ErrorType.IllegalArgument, "Expect a created account");
    }
    return accountLogic.updateAccount(client, account);
  }

  @Transactional
  public boolean changeAccountStorageState(ClientInfo client, ChangeStorageStateRequest req) {
    return accountLogic.changeStorageState(client, req);
  }

  @Transactional
  public boolean changeAccountStorageState(ClientInfo client, Account account, StorageState state) {
    return accountLogic.changeStorageState(client, account, state);
  }

  @Transactional(readOnly = true)
  public  List<Account> searchAccounts(ClientInfo client, SqlQueryParams params) {
    return accountLogic.searchAccounts(client, params); 
  }
  
  //Profile
  @Transactional(readOnly = true)
  public <T extends BaseProfile> T getProfile(ClientInfo clientInfo, String loginId) {
    return profileLogic.getProfile(clientInfo, loginId);
  }

  @Transactional
  public UserProfile saveUserProfile(ClientInfo clientInfo, UserProfile profile) {
    return profileLogic.saveUserProfile(clientInfo, profile);
  }

  @Transactional
  public OrgProfile saveOrgProfile(ClientInfo clientInfo, OrgProfile profile) {
    return profileLogic.saveOrgProfile(clientInfo, profile);
  }
  

  // education
  @Transactional(readOnly = true)
  public List<UserEducation> findUserEducation(ClientInfo clientInfo, String loginId) {
    return userLogic.findUserEducation(clientInfo, loginId);
  }

  @Transactional
  public List<UserEducation> saveUserEducations(ClientInfo clientInfo, String loginId,
      List<UserEducation> educationList) {
    return userLogic.saveUserEducations(clientInfo, loginId, educationList);
  }

  @Transactional
  public UserEducation saveUserEducation(ClientInfo client, String loginId, UserEducation education) {
    return userLogic.saveUserEducation(client, loginId, education);
  }

  @Transactional
  public boolean deleteUserEducation(ClientInfo client, String loginId, List<Long> educationIds) {
    return userLogic.deleteUserEducation(client, loginId, educationIds);
  }

  // User Identity
  @Transactional(readOnly = true)
  public List<UserIdentity> findUserIdentity(ClientInfo clientInfo, String loginId) {
    return userLogic.findUserIdentity(clientInfo, loginId);
  }

  @Transactional
  public List<UserIdentity> saveUserIdentitys(ClientInfo clientInfo, String loginId, List<UserIdentity> identityList) {
    return userLogic.saveUserIdentitys(clientInfo, loginId, identityList);
  }

  @Transactional
  public UserIdentity saveUserIdentity(ClientInfo client, String loginId, UserIdentity identity) {
    return userLogic.saveUserIdentity(client, loginId, identity);
  }

  @Transactional
  public boolean deleteUserIdentity(ClientInfo client, String loginId, List<Long> identityIds) {
    return userLogic.deleteUserIdentity(client, loginId, identityIds);
  }

  // User Work
  @Transactional(readOnly = true)
  public List<UserWork> findUserWork(ClientInfo clientInfo, String loginId) {
    return userLogic.findUserWork(clientInfo, loginId);
  }

  @Transactional
  public List<UserWork> saveUserWorks(ClientInfo clientInfo, String loginId, List<UserWork> workList) {
    return userLogic.saveUserWorks(clientInfo, loginId, workList);
  }

  @Transactional
  public UserWork saveUserWork(ClientInfo client, String loginId, UserWork work) {
    return userLogic.saveUserWork(client, loginId, work);
  }

  @Transactional
  public boolean deleteUserWork(ClientInfo client, String loginId, List<Long> workIds) {
    return userLogic.deleteUserWork(client, loginId, workIds);
  }

  @Transactional(readOnly = true)
  public Account getAccount(ClientInfo client, String loginId) {
    return accountLogic.getModifiableAccount(client, loginId);
  }

  // User Relation
  @Transactional(readOnly = true)
  public List<UserRelation> findUserRelation(ClientInfo clientInfo, String loginId) {
    return userLogic.findUserRelations(clientInfo, loginId);
  }

  @Transactional
  public List<UserRelation> saveUserRelations(ClientInfo clientInfo, String loginId, List<UserRelation> relationList) {
    return userLogic.saveUserRelations(clientInfo, loginId, relationList);
  }

  @Transactional
  public UserRelation saveUserRelation(ClientInfo client, String loginId, UserRelation relation) {
    return userLogic.saveUserRelation(client, loginId, relation);
  }

  @Transactional
  public boolean deleteUserRelation(ClientInfo client, String loginId, List<Long> relationIds) {
    return userLogic.deleteUserRelation(client, loginId, relationIds);
  }

  // Banks Account
  @Transactional(readOnly =  true)
  public List<BankAccount> findBankAccount(ClientInfo clientInfo, String loginId) {
    return userLogic.findBankAccounts(clientInfo, loginId );
  }

  @Transactional
  public List<BankAccount> saveBankAccounts(ClientInfo clientInfo, String loginId, List<BankAccount> accountList) {
    return userLogic.saveBankAccounts(clientInfo, loginId, accountList);
  }

  @Transactional
  public BankAccount saveBankAccount(ClientInfo client, String loginId, BankAccount bank) {
    return userLogic.saveBankAccount(client, loginId, bank);
  }

  @Transactional
  public boolean deleteBankAccount(ClientInfo client, String loginId, List<Long> bankIds) {
    return userLogic.deleteBankAccount(client, loginId, bankIds);
  }
  
  @Transactional
  public List<BankAccount> searchBankAccount(ClientInfo client, SqlQueryParams params) {
    return userLogic.searchBankAccounts(client, params);
  }
  // Account password
  @Transactional
  public Result<Boolean> changePassword(ClientInfo client, ChangePasswordRequest request) {
    return accountLogic.changePassword(client, request);
  }
  @Transactional
  public Result<Boolean> resetPassword(ClientInfo client, ResetPasswordRequest request) {
    return accountLogic.resetPassword(client, request);
  }
}
