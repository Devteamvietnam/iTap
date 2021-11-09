package com.devteam.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SQLDialectHelper {
  static public enum DbType {hsqldb, postgres}

  @Value("${db.jdbc.driver:org.hsqldb.jdbcDriver}")
  private String jdbcDriver;

  private DbType dbtype = DbType.hsqldb;

  @PostConstruct
  public void onInit() {
    if(jdbcDriver.indexOf("hsqldb") > 0) {
      dbtype = DbType.hsqldb;
    } else {
      dbtype = DbType.postgres;
    }
  }

  public String string_agg(String field, String separator) {
    if(dbtype == DbType.hsqldb) {
      //return "'Not Supported'";
      return "group_concat(DISTINCT " + field + " ORDER BY " + field + " DESC SEPARATOR ',')" ;
    }
    return "string_agg(" + field + ", '" + separator + "')" ;
  }
}
