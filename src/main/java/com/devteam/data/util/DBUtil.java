package com.devteam.data.util;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class DBUtil {
//  static public DataSource createHsqlDbDs(String username, String password, String url) {
//    return createDs(org.hsqldb.jdbcDriver.class.getName(), username, password, url);
//  }

  static DataSource createDs(String driver, String username, String password, String url) {
    DataSourceBuilder<?> builder =
        DataSourceBuilder.create().
        type(DriverManagerDataSource.class).
        username(username).password(password).url(url).
        driverClassName(driver);
    return builder.build();
  }
}
