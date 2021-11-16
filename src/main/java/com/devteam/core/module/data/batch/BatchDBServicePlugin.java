package com.devteam.core.module.data.batch;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.module.data.db.DBServicePlugin;
import com.devteam.core.util.io.IOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(value = DBServicePlugin.SETTINGS)
@Slf4j
public class BatchDBServicePlugin extends DBServicePlugin {
  @Value("${spring.batch.datasource.db-type:hsqldb}")
  private String dsType;

  @Autowired
  private DataSource ds;  

  @Override
  public void initDb(ClientInfo client, ApplicationContext context) throws Exception {
    log.info("Create Batch Entity Manager Factory");
    try {
      String scriptFile = "org/springframework/batch/core/schema-" + dsType + ".sql";
      log.info("Batch Init Script File: {}", scriptFile);
      String script = IOUtil.getResourceAsString(scriptFile, "UTF-8");
      Connection conn = ds.getConnection();
      Statement st = conn.createStatement();
      st.execute(script);
      conn.commit();
    } catch (Exception e) {
      log.error("Create Batch Table Error", e);
    }
  }
}
