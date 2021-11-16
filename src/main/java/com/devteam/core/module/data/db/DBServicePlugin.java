package com.devteam.core.module.data.db;

import com.devteam.core.module.common.ClientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DBServicePlugin {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  
  final static public int CORE_SECURITY = 0;
  final static public int SETTINGS      = 1;

  final static public int ACCOUNT  = 10;

  final static public int COMPANY  = 20;
  final static public int COMPANY_HR  = 30;
  final static public int COMMUNICATION = 40;

  final static public int PARTNER  = 50;
  final static public int PROJECT    = 60;

  final static public int COMPANY_MISC  = 61;
  
  final static public int ACCOUNTING  = 70;
  final static public int ACCOUNTING_HR  = 71;

  
  final static public int ASSET  = 80;
  final static public int PRODUCT  = 90;


  final static public int LEGACY  = 150;
  
  final static public int APP  = 200;

  public void initDb(ClientInfo client, ApplicationContext context) throws Exception {
  }

  public void createSammpleData(ClientInfo client, ApplicationContext context) throws Exception {
  }

  public void postInitDb(ClientInfo client, DBService service, ApplicationContext context, boolean initSample) throws Exception {
  }
  
  public <T> void initCompanyDb(ClientInfo client, T companyCtx,  ApplicationContext context) throws Exception {
  }
  
  public <T> void createSammpleData(ClientInfo client, T companyCtx, ApplicationContext context) throws Exception {
  }
}
