package com.devteam.module.data.io;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.DBScenario;
import com.devteam.core.module.data.db.DBServicePlugin;
import com.devteam.module.data.io.db.sample.DBModuleScenario;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(value = DBServicePlugin.ACCOUNT)
public class DataIODBServicePlugin extends DBServicePlugin {

  public <T> void createSammpleData(ClientInfo client, T companyCtx, ApplicationContext context) throws Exception {
    new DBScenario(context).add(DBModuleScenario.class).initialize(client);
  }
}
