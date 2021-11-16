package com.devteam.module.account.http;

import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpSession;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.query.SqlQueryParams;
import com.devteam.core.module.http.rest.RestResponse;
import com.devteam.core.module.http.rest.v1.AuthenticationService;
import com.devteam.core.module.http.rest.v1.BaseController;
import com.devteam.module.account.AccountService;
import com.devteam.module.account.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(value="devteam", tags= {"account"})
@RestController
@ConditionalOnBean(AuthenticationService.class)
@RequestMapping("/rest/v1.0.0/account")
public class UserController extends BaseController {
  @Autowired
  private AccountService service;

  protected UserController() {
    super("account", "/account");
  }

  //user Education
  @ApiOperation(value = "Retrieve the user-educations by loginId", responseContainer = "List", response = UserEducation.class)
  @GetMapping("user-education/{loginId}/find")
  public @ResponseBody RestResponse findUserEducations(
      HttpSession session, @PathVariable("loginId") String loginId) {
    Callable<List<UserEducation>> executor = () -> {
      ClientInfo clientInfo = getAuthorizedClientInfo(session);
      return service.findUserEducation(clientInfo, loginId);
    };
    return execute(Method.GET, "user-education/{loginId}/find", executor);
  }

  @ApiOperation(value = "Save user education", response = UserEducation.class)
  @PutMapping("user-education/{loginId}/save")
  public @ResponseBody RestResponse saveUserEducation(
      HttpSession session, @PathVariable("loginId") String loginId, @RequestBody UserEducation education) {
    Callable<UserEducation> executor = () -> {
      ClientInfo client = getAuthorizedClientInfo(session);
      return service.saveUserEducation(client, loginId, education);
    };
    return execute(Method.PUT, "user-education/{loginId}/save", executor);
  }
  
  @ApiOperation(value = "Delete the user education", response = Boolean.class)
  @DeleteMapping("user-education/{loginId}")
  public @ResponseBody RestResponse deleteUserEducation(
      HttpSession session, @PathVariable("loginId") String loginId, @RequestBody List<Long> educationIds) {
    Callable<Boolean> executor = () -> {
      ClientInfo client = getAuthorizedClientInfo(session);
      service.deleteUserEducation(client, loginId, educationIds);
      return true;
    };
    return execute(Method.DELETE, "user-education/{loginId}", executor);
  }


// User Identity
  @ApiOperation(value = "Retrieve the user-identities by loginId", responseContainer = "List", response = UserWork.class)
  @GetMapping("user-identity/{loginId}/find")
  public @ResponseBody RestResponse findUserIdentity(HttpSession session, @PathVariable("loginId") String loginId) {
    Callable<List<UserIdentity>> executor = () -> {
      ClientInfo clientInfo = getAuthorizedClientInfo(session);
      return service.findUserIdentity(clientInfo, loginId);
    };
    return  execute(Method.GET, "user-identity/{loginId}/find", executor);
  }

  @ApiOperation(value = "Save user Identiity", response = UserIdentity.class)
  @PutMapping("user-identity/{loginId}/save")
  public @ResponseBody RestResponse saveUserIdentity(HttpSession session, @PathVariable("loginId") String loginId,
      @RequestBody UserIdentity identity) {
    Callable<UserIdentity> executor = () -> {
      ClientInfo client = getAuthorizedClientInfo(session);
      return service.saveUserIdentity(client, loginId, identity);
    };
    return execute(Method.PUT, "user-identity/{loginId}/save", executor);
  }

  @ApiOperation(value = "Delete the user identity", response = Boolean.class)
  @DeleteMapping("user-identity/{loginId}")
  public @ResponseBody RestResponse deleteUserIdentity(HttpSession session, @PathVariable("loginId") String loginId,
      @RequestBody List<Long> identityIds) {
    Callable<Boolean> executor = () -> {
      ClientInfo client = getAuthorizedClientInfo(session);
      service.deleteUserIdentity(client, loginId, identityIds);
      return true;
    };
    return execute(Method.DELETE, "user-identity/{loginId}", executor);
  }

  // user work
  @ApiOperation(value = "Retrieve the user-work by loginId", responseContainer = "List", response = UserWork.class)
  @GetMapping("user-work/{loginId}/find")
  public @ResponseBody RestResponse findUserWork(HttpSession session, @PathVariable("loginId") String loginId) {
    Callable<List<UserWork>> executor = () -> {
      ClientInfo clientInfo = getAuthorizedClientInfo(session);
      return service.findUserWork(clientInfo, loginId);
    };
    return execute(Method.GET, "user-works/{loginId}/find", executor);
  }


  @ApiOperation(value = "Save user work", response = UserWork.class)
  @PutMapping("user-work/{loginId}/save")
  public @ResponseBody RestResponse saveUserIdentity(HttpSession session, @PathVariable("loginId") String loginId,
      @RequestBody UserWork work) {
    Callable<UserWork> executor = () -> {
      ClientInfo client = getAuthorizedClientInfo(session);
      return service.saveUserWork(client, loginId, work);
    };
    return execute(Method.PUT, "user-work/{loginId}/save", executor);
  }

  @ApiOperation(value = "Delete the user work", response = Boolean.class)
  @DeleteMapping("user-work/{loginId}")
  public @ResponseBody RestResponse deleteUserWork(HttpSession session, @PathVariable("loginId") String loginId,
      @RequestBody List<Long> workIds) {
    Callable<Boolean> executor = () -> {
      ClientInfo client = getAuthorizedClientInfo(session);
      service.deleteUserWork(client, loginId, workIds);
      return true;
    };
    return execute(Method.DELETE, "user-work/{loginId}", executor);
  }

  // user relation
  @ApiOperation(value = "Retrieve the user-relations by loginId", responseContainer = "List", response = UserRelation.class)
  @GetMapping("user-relation/{loginId}/find")
  public @ResponseBody RestResponse findUserRelation(HttpSession session, @PathVariable("loginId") String loginId) {
    Callable<List<UserRelation>> executor = () -> {
      ClientInfo clientInfo = getAuthorizedClientInfo(session);
      return service.findUserRelation(clientInfo, loginId);
    };
    return execute(Method.GET, "user-relation/{loginId}/find", executor);
  }


  @ApiOperation(value = "Save user relation", response = UserRelation.class)
  @PutMapping("user-relation/{loginId}/save")
  public @ResponseBody RestResponse saveUserRelation(HttpSession session, @PathVariable("loginId") String loginId,
      @RequestBody UserRelation relation) {
    Callable<UserRelation> executor = () -> {
      ClientInfo client = getAuthorizedClientInfo(session);
      return service.saveUserRelation(client, loginId, relation);
    };
    return execute(Method.PUT, "user-relation/{loginId}/save", executor);
  }

  @ApiOperation(value = "Delete the user relation", response = Boolean.class)
  @DeleteMapping("user-relation/{loginId}")
  public @ResponseBody RestResponse deleteUserRelation(HttpSession session, @PathVariable("loginId") String loginId,
      @RequestBody List<Long> relationIds) {
    Callable<Boolean> executor = () -> {
      ClientInfo client = getAuthorizedClientInfo(session);
      service.deleteUserRelation(client, loginId, relationIds);
      return true;
    };
    return execute(Method.DELETE, "user-relation/{loginId}", executor);
  }

  // Bank Account
  @ApiOperation(value= "Retrieve the bank-accounts by loginId", responseContainer = "List", response = BankAccount.class)
  @GetMapping("bank-account/{loginId}/find")
  public @ResponseBody RestResponse findBankAccount(HttpSession session, @PathVariable("loginId") String loginId) {
    Callable<List<BankAccount>> executor = () -> {
      ClientInfo clientInfo = getAuthorizedClientInfo(session);
      return service.findBankAccount(clientInfo, loginId);
    };
    return  execute(Method.GET, "bank-account/{loginId}/find", executor);
  }

  @ApiOperation(value = "Save bank account", response = BankAccount.class)
  @PutMapping("bank-account/{loginId}/save")
  public @ResponseBody RestResponse saveBankAccount(HttpSession session, @PathVariable("loginId") String loginId,
      @RequestBody BankAccount bank) {
    Callable<BankAccount> executor = () -> {
      ClientInfo client = getAuthorizedClientInfo(session);
      return service.saveBankAccount(client, loginId, bank);
    };
    return execute(Method.PUT, "bank-account/{loginId}/save", executor);
  }

  @ApiOperation(value = "Delete the bank account", response = Boolean.class)
  @DeleteMapping("bank-account/{loginId}")
  public @ResponseBody RestResponse deleteBankAccount(HttpSession session, @PathVariable("loginId") String loginId,
      @RequestBody List<Long> bankIds) {
    Callable<Boolean> executor = () -> {
      ClientInfo client = getAuthorizedClientInfo(session);
      service.deleteBankAccount(client, loginId, bankIds);
      return true;
    };
    return execute(Method.DELETE, "bank-account/{loginId}", executor);
  }
  
  @ApiOperation(value = "Search Bank Account", responseContainer = "List", response = BankAccount.class)
  @PostMapping("bank-account/search")
  public @ResponseBody RestResponse searchLocations(HttpSession session, @RequestBody SqlQueryParams params) {
    Callable<List<BankAccount>> executor = () -> {
      ClientInfo client = getAuthorizedClientInfo(session);
      return service.searchBankAccount(client, params);
    };
    return execute(Method.POST, "bank-account/search", executor);
  }
}