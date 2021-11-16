package com.devteam.module.account;

import java.util.List;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.query.SqlQueryParams;
import com.devteam.core.module.http.get.GETContent;
import com.devteam.core.module.http.get.GETTmpStoreHandler;
import com.devteam.core.module.http.get.StoreInfo;
import com.devteam.core.module.misc.template.TemplateService;
import com.devteam.core.util.ds.MapObject;
import com.devteam.module.account.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountPrintService {
  @Autowired
  private TemplateService templateService;

  @Autowired
  private AccountService accountService;
  
  @Autowired
  private GETTmpStoreHandler tmpStoreHandler;
  
  public StoreInfo activities(ClientInfo client, String format, SqlQueryParams params) {
    List<Account> accounts = accountService.searchAccounts(client, params);
    MapObject scopes = new MapObject("accounts", accounts);
    byte[] data = templateService.render("account/activities.hbs", format, scopes);
    GETContent getContent = new GETContent("activities." + format, data);
    return tmpStoreHandler.store(client, getContent);
  }
  
  public StoreInfo statistics(ClientInfo client, String format, SqlQueryParams params) {
    List<Account> accounts = accountService.searchAccounts(client, params);
    MapObject scopes = new MapObject("accounts", accounts);
    byte[] data = templateService.render("account/statistics.hbs", format, scopes);
    GETContent getContent = new GETContent("statistics." + format, data);
    return tmpStoreHandler.store(client, getContent);
  }
}
