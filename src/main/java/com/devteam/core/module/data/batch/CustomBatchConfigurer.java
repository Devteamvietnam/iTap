package com.devteam.core.module.data.batch;

import javax.sql.DataSource;

import com.devteam.config.JpaConfiguration;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Import(value = { JpaConfiguration.class })
@Slf4j
public class CustomBatchConfigurer implements BatchConfigurer {
  @Autowired
  private DataSource dataSource;
  
  @Autowired
  private PlatformTransactionManager transactionManager;
  
  @Override
  public JobRepository getJobRepository() throws Exception {
    log.info("Create Batch JobRepositoryFactoryBean");
    JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
    factoryBean.setDataSource(dataSource);
    factoryBean.setTransactionManager(getTransactionManager());
    factoryBean.afterPropertiesSet();
    return factoryBean.getObject();
  }

  @Override
  public PlatformTransactionManager getTransactionManager() { return transactionManager; }

  @Override
  public JobLauncher getJobLauncher() throws Exception {
    SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
    jobLauncher.setJobRepository(getJobRepository());
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }

  @Override
  public JobExplorer getJobExplorer() throws Exception {
    JobExplorerFactoryBean factory = new JobExplorerFactoryBean();
    factory.setDataSource(dataSource);
    factory.afterPropertiesSet();
    return factory.getObject();
  }
}