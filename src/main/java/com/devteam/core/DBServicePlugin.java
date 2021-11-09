package com.devteam.core;

import com.devteam.module.common.ClientInfo;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

@Getter
@Setter
public class DBServicePlugin {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  final static public int CORE_SECURITY = 0;
  final static public int ACCOUNT = 10;

  public void initDb(ClientInfo client, ApplicationContext context) throws Exception {
  }

  public void createSammpleData(ClientInfo client, ApplicationContext context) throws Exception {
  }

  public void postInitDb(ClientInfo client, DBService service, ApplicationContext context, boolean initSample)
      throws Exception {
  }

  public <T> void initDb(ClientInfo client, T devCtx, ApplicationContext context) throws Exception {
  }

  public <T> void createSammpleData(ClientInfo client, T devCtx, ApplicationContext context) throws Exception {
  }
}
