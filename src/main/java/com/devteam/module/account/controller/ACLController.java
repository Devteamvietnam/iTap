package com.devteam.module.account.controller;

import com.devteam.core.enums.AccessType;
import com.devteam.module.account.service.AccountService;
import com.devteam.module.common.ClientInfo;
import com.devteam.module.http.ClientSession;
import com.devteam.module.http.rest.RestResponse;
import com.devteam.module.http.rest.v1.BaseController;
import com.devteam.security.ACLModel;
import com.devteam.security.AccountAclModel;
import com.devteam.security.SecurityService;
import com.devteam.security.entity.AccessToken;
import com.devteam.security.entity.AppAccessPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.Callable;

@CrossOrigin(value = "*")
@Api(value = "devteam", tags = { "acl" })
@RestController
@RequestMapping("/api/v1/acl")
@Slf4j
public class ACLController extends BaseAccountController {
    @Getter
    @Setter
    static public class LoginModel {
        private String tenantId;
        private String loginId;
        private String password;
        private int timeToLiveInMin;
        private AccessType accessType = AccessType.ACCOUNT;
    }

    @Autowired
    protected AccountService accountService;

    @Autowired
    protected SecurityService securityService;

    public ACLController() {
        super("account", "/acl");
    }

    @ApiOperation(value = "Account ACL aunthenticate", response = ACLModel.class)
    @PostMapping("authenticate")
    public @ResponseBody
    RestResponse authenticate(HttpServletRequest httpReq, @RequestBody LoginModel model)
            throws Exception {
        Callable<ACLModel> executor = () -> {
            HttpSession session = httpReq.getSession(true);
            String loginId = model.getLoginId();
            ClientInfo client = new ClientInfo(model.getTenantId(), loginId, httpReq.getRemoteAddr(), session.getId());
            AccessToken token = accountService.authenticate(client, loginId, model.getPassword(), 24 * 60);
            ACLModel accountAcl = new ACLModel(token);
            return doLogin(session, client, accountAcl);
        };
        return execute(BaseController.Method.POST, "authenticate", executor);
    }

    @ApiOperation(value = "Account ACL aunthenticate", response = ACLModel.class)
    @PostMapping("token/validate")
    public @ResponseBody RestResponse validate(HttpServletRequest httpReq, @RequestBody AccessToken token)
            throws Exception {
        Callable<ACLModel> executor = () -> {
            HttpSession session = httpReq.getSession(true);
            ClientInfo client = new ClientInfo("default", "system", httpReq.getRemoteAddr(), session.getId());
            AccessToken accessToken = securityService.validateAccessToken(client, token.getToken());
            ACLModel accountAcl = new ACLModel(accessToken);
            return doLogin(session, client, accountAcl);
        };
        return execute(BaseController.Method.POST, "token/validate", executor);
    }

    @ApiOperation(value = "Account ACL logout", response = Boolean.class)
    @GetMapping("logout")
    public @ResponseBody RestResponse logout(HttpSession session) throws Exception {
        Callable<Boolean> executor = () -> {
            ClientSession authSession = doLogout(session);
            return authSession != null;
        };
        return execute(BaseController.Method.GET, "logout", executor);
    }

    protected ACLModel doLogin(HttpSession session, ClientInfo client, ACLModel accountAcl) throws Exception {
        String loginId = accountAcl.getAccessToken().getLoginId();
        client.setRemoteUser(loginId);
        if (accountAcl.getAccessToken().isAuthorized()) {
            ClientSession clientSession = doLogin(session, client);
            List<AccountAclModel> contexts = accountService.findAccountAcls(client, loginId);
            accountAcl.withAvailableAccountAcls(contexts);
            AccountAclModel accountAcls = accountAcl.getAccountAcl();
            if (accountAcl != null) {
                List<AppAccessPermission> appPermissions = securityService.findPermissions(client,
                        AccessType.ACCOUNT, accountAcls.getLoginId());
                accountAcls.setAppPermissions(appPermissions);
                clientSession.setBean(AccountAclModel.class, accountAcls);
            }

            accountAcl.setSessionId(session.getId());
            log.info("User {} is logged in successfully into {} tenant system", loginId, client.getClientId());
        } else {
            log.info("User {} try to login into {} tenant system, but fail", loginId, client.getTenantId());
        }
        return accountAcl;
    }

    @ApiOperation(value = "Change  Context", response = AccountAclModel.class)
    @PostMapping("change")
    public @ResponseBody RestResponse change(HttpSession session, @RequestBody AccountAclModel ctx)
            throws Exception {
        Callable<AccountAclModel> executor = () -> {
            return doChange(session, ctx);
        };
        return execute(BaseController.Method.POST, "change", executor);
    }
}
