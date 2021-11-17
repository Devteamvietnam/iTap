package com.devteam.module.company.core.entity;


import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.query.SqlQueryParams;

public class ShareableUtil {

  public static SqlQueryParams enrichShareableParams(ClientInfo client, Company company, SqlQueryParams params) {
    params.addParam("companyId", company.getId());
    params.addParam("companyIds", company.findCompanyIdPaths());
    params.addParam("loginId", client.getRemoteUser());
    params.addParam("privateShareable", ShareableScope.PRIVATE.toString());
    params.addParam("companyShareable", ShareableScope.COMPANY.toString());
    params.addParam("desShareable", ShareableScope.DESCENDANTS.toString());
    params.addParam("orgShareable", ShareableScope.ORGANIZATION.toString());
    return params;
  }
}
