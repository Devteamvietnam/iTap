package com.devteam.module.account.http;

import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpSession;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.http.rest.RestResponse;
import com.devteam.core.module.http.rest.v1.AuthenticationService;
import com.devteam.core.module.http.rest.v1.BaseController;
import com.devteam.core.module.http.upload.UploadResource;
import com.devteam.module.account.AccountService;
import com.devteam.module.account.entity.BaseProfile;
import com.devteam.module.account.entity.OrgProfile;
import com.devteam.module.account.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class ProfileController extends BaseController {
  @Autowired
  private AccountService service;

  protected ProfileController() {
    super("account", "/account");
  }

  @ApiOperation(value = "Retrieve Account profile", response = BaseProfile.class)
  @GetMapping("profile/{loginId}")
  public @ResponseBody <T extends BaseProfile> RestResponse getProfile(HttpSession session, @PathVariable("loginId") String loginId) {
    Callable<T> executor = () -> {
      ClientInfo clientInfo = getAuthorizedClientInfo(session);
      return service.getProfile(clientInfo, loginId);
    };
    return execute(Method.GET, "profile/{loginId}", executor);
  }

  @ApiOperation(value = "Save the user profile", response = UserProfile.class)
  @PutMapping("profile/user")
  public @ResponseBody RestResponse saveUserProfile(HttpSession session, @RequestBody UserProfile profile) {
    Callable<UserProfile> executor = () -> {
      ClientInfo clientInfo = getAuthorizedClientInfo(session);
      return service.saveUserProfile(clientInfo, profile);
    };
    return execute(Method.PUT, "profile/user", executor);
  }

  @ApiOperation(value = "Save the user profile", response = UserProfile.class)
  @PutMapping("profile/org")
  public @ResponseBody RestResponse saveOrgProfile(HttpSession session, @RequestBody OrgProfile profile) {
    Callable<OrgProfile> executor = () -> {
      ClientInfo clientInfo = getAuthorizedClientInfo(session);
      return service.saveOrgProfile(clientInfo, profile);
    };
    return execute(Method.PUT, "profile/user", executor);
  }

  @ApiOperation(value = "Upload user avatar", response = UploadResource.class)
  @PutMapping("profile/{loginId}/upload-avatar")
  public @ResponseBody RestResponse uploadAvatar(
      HttpSession session, @PathVariable("loginId") String loginId, @RequestBody UploadResource resource) {
    Callable<UploadResource> executor = () -> {
      ClientInfo clientInfo = getAuthorizedClientInfo(session);
      return service.uploadAvatar(clientInfo, loginId, resource, true);
    };
    return execute(Method.PUT, "profile/user", executor);
  }

  @ApiOperation(value = "Upload user avatar", response = UploadResource.class)
  @PutMapping("profile/{loginId}/modify-avatar")
  public @ResponseBody RestResponse modifyAvatar(
      HttpSession session, @PathVariable("loginId") String loginId, @RequestBody UploadResource resource) {
    Callable<UploadResource> executor = () -> {
      ClientInfo clientInfo = getAuthorizedClientInfo(session);
      return service.uploadAvatar(clientInfo, loginId, resource, false);
    };
    return execute(Method.PUT, "profile/user", executor);
  }

}