package com.devteam.security;

import com.devteam.core.enums.AccessType;
import com.devteam.core.query.SqlQueryParams;
import com.devteam.module.common.ClientInfo;
import com.devteam.security.entity.AccessToken;
import com.devteam.security.entity.App;
import com.devteam.security.entity.AppAccessPermission;
import com.devteam.security.entity.AppPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SecurityService {

  @Autowired
  SecurityLogic logic;

  @Transactional(readOnly = true)
  public App getApp(ClientInfo client, String module, String name) {
    return logic.getApp(client, module, name);
  }

  @Transactional
  public App saveApp(ClientInfo client, App app) {
    return logic.saveApp(client, app);
  }

  @Transactional(readOnly = true)
  public List<App> findApps(ClientInfo client) {
    return logic.findApps(client);
  }

  @Transactional
  public AppPermission saveAppPermisson(ClientInfo client, AppPermission permisson) {
    return logic.saveAppPermisson(client, permisson);
  }

  @Transactional
  public List<AppPermission> saveAppPermissions(ClientInfo client, List<AppPermission> permissions) {
    return logic.saveAppPermissions(client, permissions);
  }

  @Transactional
  public List<AppPermission> findAppPermissions(ClientInfo client, String loginId) {
    return logic.findAppPermissions(client, loginId);
  }

  @Transactional
  public boolean deletePermissions(ClientInfo client, List<AppPermission> permissions) {
    return logic.deletePermissions(client, permissions);
  }

  @Transactional
  public boolean deletePermissionsById(ClientInfo client, List<Long> permissionsIds) {
    return logic.deletePermissionsByIds(client, permissionsIds);
  }

  @Transactional(readOnly=true)
  public List<AppAccessPermission> findPermissions(ClientInfo client, AccessType accessType, String loginId) {
    return logic.findPermissions(client, accessType, loginId);
  }

  @Transactional(readOnly=true)
  public List<AppAccessPermission> searchPermissions(ClientInfo client, SqlQueryParams params) {
    return logic.searchPermissions(client, params);
  }

  //Access Token

  @Transactional(readOnly=true)
  public AccessToken validateAccessToken(ClientInfo client, String token) {
    AccessToken accessToken = logic.getAccessToken(client, token);
    if(accessToken == null || accessToken.isExpired()) {
      return new AccessToken("ddthien-digital-none", AccessToken.AccessType.None);
    }
    return accessToken;
  }


  @Transactional(readOnly=true)
  public AccessToken getAccessToken(ClientInfo client, String token) {
    return logic.getAccessToken(client, token);
  }

  @Transactional
  public AccessToken saveAccessToken(ClientInfo client, AccessToken token) {
    return logic.saveAccessToken(client, token);
  }

  @Transactional(readOnly=true)
  public List<AccessToken> searchAccessTokens(ClientInfo client, SqlQueryParams params) {
    return logic.searchAccessTokens(client, params);
  }
}
