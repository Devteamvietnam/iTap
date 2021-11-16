package com.devteam.core.module.data.batch;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.devteam.core.module.srpingframework.app.AppEnv;
import com.devteam.core.util.dataformat.DataSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

/*
@Configuration
@ComponentScan(basePackages = {
    "net.datatp.module.data.batch",
   }
)
@EnableJpaRepositories(
    basePackages  = {
      "net.datatp.module.data.batch",
      "net.datatp.module.data.repository",
    }
)
@EnableConfigurationProperties
@EnableTransactionManagement
@EnableAutoConfiguration
@Import(value = {
  SpringFrameworkModuleConfig.class
})
*/
@Slf4j
public class BatchJpaConfiguration {

  @Autowired
  private AppEnv appEnv;

  @Bean("batchDatasource")
  @ConfigurationProperties("spring.batch.datasource")
  public DataSource batchDataSource() {
    log.info("Create Batch Datasource");
    Map<String, Object> config = appEnv.getPropertiesWithPrefix("spring.batch.datasource");
    String jsonConfig = DataSerializer.JSON.toString(config);
    log.info("Batch Data Source: \n {}".concat(jsonConfig));
    DataSource ds = DataSourceBuilder.create().type(HikariDataSource.class).build();
    return ds;
  }

  @Bean("batch-validator")
  public LocalValidatorFactoryBean createLocalValidatorFactoryBean() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    return bean;
  }
  
  @Bean("batchEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
      @Value("${spring.batch.hibernate.hbm2ddl.auto:update}") String hbm2ddlAuto,
      @Value("${spring.batch.hibernate.dialect:org.hibernate.dialect.HSQLDialect}") String hibernateDialect,
      @Value("${spring.batch.hibernate.show_sql:false}") String hibernateShowSql,
      @Qualifier("batchDatasource") DataSource ds,  
      @Qualifier("batch-validator") LocalValidatorFactoryBean validator,
      //CustomHibernateInterceptor interceptor, 
      Environment environment) {

    LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
    factoryBean.setDataSource(ds);

    String[] moduleBasePkgs = { "org.springframework.batch.core" };
    factoryBean.setPackagesToScan(moduleBasePkgs);

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    factoryBean.setJpaVendorAdapter(vendorAdapter);

    HashMap<String, Object> jpaPropMap = new HashMap<>();
    jpaPropMap.put("validator", validator);
    //jpaPropMap.put("hibernate.session_factory.interceptor", interceptor);
    jpaPropMap.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
    jpaPropMap.put("hibernate.dialect", hibernateDialect);
    jpaPropMap.put("hibernate.show_sql", hibernateShowSql);
    jpaPropMap.put("hibernate.format_sql", "true");
    jpaPropMap.put("hibernate.enable_lazy_load_no_trans", "true");
    jpaPropMap.put("hibernate.connection.provider_disables_autocommit", "false");
    factoryBean.setJpaPropertyMap(jpaPropMap);
    return factoryBean;
  }
  
  @Bean("batchTransactionManager")
  public PlatformTransactionManager transactionManager(
      @Qualifier("batchEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
    log.info("Create PlatformTransactionManager name = batchTransactionManager");
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(factory.getObject());
    return transactionManager;
  }
}