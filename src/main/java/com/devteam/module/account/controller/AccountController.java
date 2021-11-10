package com.devteam.module.account.controller;

import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpSession;

import com.devteam.core.entity.ChangeStorageStateRequest;
import com.devteam.core.query.SqlQueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.devteam.module.account.service.AccountService;
import com.devteam.module.account.model.ChangePasswordRequest;
import com.devteam.module.account.model.NewAccountModel;
import com.devteam.module.account.model.ResetPasswordRequest;
import com.devteam.module.account.entity.Account;
import com.devteam.module.common.Result;
import com.devteam.module.http.rest.RestResponse;
import com.devteam.module.http.rest.v1.AuthenticationService;
import com.devteam.module.http.rest.v1.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="devteam", tags= {"account"})
@RestController
@ConditionalOnBean(AuthenticationService.class)
@RequestMapping("/api/v1/account")
public class AccountController extends BaseController {
    @Autowired
    private AccountService service;

    protected AccountController() {
        super("account", "/account");
    }


    @ApiOperation(value = "Persist the account model", response = NewAccountModel.class)
    @PutMapping("account/create")
    public @ResponseBody RestResponse createAccount(HttpSession session, @RequestBody NewAccountModel model) {
        Callable<NewAccountModel> executor = () -> {
            return service.createNewAccount(getAuthorizedClientInfo(session), model);
        };
        return execute(Method.PUT, "account", executor);
    }

    @ApiOperation(value = "Persist the account", response = Account.class)
    @PutMapping("account")
    public @ResponseBody RestResponse saveAccount(HttpSession session, @RequestBody Account account) {
        Callable<Account> executor = () -> {
            return service.saveAccount(getAuthorizedClientInfo(session), account);
        };
        return execute(Method.PUT, "account", executor);
    }

    @ApiOperation(value = "Change the storage state", response = Boolean.class)
    @PutMapping("account/storage-state")
    public @ResponseBody RestResponse changeStorageState(HttpSession session, @RequestBody ChangeStorageStateRequest req) {
        Callable<Boolean> executor = () -> {
            return service.changeAccountStorageState(getAuthorizedClientInfo(session), req);
        };
        return execute(Method.PUT, "account/storage-state", executor);
    }

    @ApiOperation(value = "Search Accounts", responseContainer = "List", response = Account.class)
    @PostMapping("account/search")
    public @ResponseBody RestResponse  searchAccounts(
            HttpSession session, @RequestBody SqlQueryParams params) {
        Callable<List<Account>> executor = () -> {
            return service.searchAccounts(getAuthorizedClientInfo(session), params);
        };
        return execute(Method.POST, "account/search", executor);
    }

    @ApiOperation(value = "Change Password the account", response = Boolean.class)
    @PutMapping("change-password")
    public @ResponseBody RestResponse changePassword(HttpSession session, @RequestBody ChangePasswordRequest request) {
        Callable<Result<Boolean>> executor = () -> {
            return service.changePassword(getAuthorizedClientInfo(session), request);
        };
        return execute(Method.PUT, "change-password", executor);
    }

    @ApiOperation(value = "Reset Password the account", response = Boolean.class)
    @PutMapping("reset-password")
    public @ResponseBody RestResponse resetPassword(HttpSession session, @RequestBody ResetPasswordRequest request) {
        Callable<Result<Boolean>> executor = () -> {
            return service.resetPassword(getAuthorizedClientInfo(session), request);
        };
        return execute(Method.PUT, "reset-password", executor);
    }
}
