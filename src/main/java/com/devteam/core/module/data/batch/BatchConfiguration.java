package com.devteam.core.module.data.batch;


import java.util.concurrent.atomic.AtomicReference;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.AbstractLazyCreationTargetSource;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.JobScope;
import org.springframework.batch.core.scope.StepScope;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;



@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = {
    "com.devteam.core.module.data",
   }
)
@Import(
    value = { 
        //BatchJpaConfiguration.class,
        CustomBatchConfigurer.class, 
        CustomScopeConfiguration.class 
     }
)
@Slf4j
public class BatchConfiguration  implements ImportAware, InitializingBean {
  @Autowired
  private CustomBatchConfigurer configurer;
  
  private JobBuilderFactory jobBuilderFactory;

  private StepBuilderFactory stepBuilderFactory;
  
  private boolean initialized = false;

  private AtomicReference<JobRepository> jobRepository = new AtomicReference<>();

  private AtomicReference<JobLauncher> jobLauncher = new AtomicReference<>();

  private AtomicReference<JobRegistry> jobRegistry = new AtomicReference<>();

  private AtomicReference<PlatformTransactionManager> transactionManager = new AtomicReference<>();


  private AtomicReference<JobExplorer> jobExplorer = new AtomicReference<>();
  
  
  @Bean
  public JobBuilderFactory jobBuilders() throws Exception {
    return this.jobBuilderFactory;
  }

  @Bean
  public StepBuilderFactory stepBuilders() throws Exception {
    return this.stepBuilderFactory;
  }

  @Bean
  public JobRepository jobRepository() throws Exception {
    return createLazyProxy(jobRepository, JobRepository.class);
  }

  @Bean
  public JobLauncher jobLauncher() throws Exception {
    return createLazyProxy(jobLauncher, JobLauncher.class);
  }

  @Bean
  public JobRegistry jobRegistry() throws Exception {
    return createLazyProxy(jobRegistry, JobRegistry.class);
  }

  @Bean
  public JobExplorer jobExplorer() {
    return createLazyProxy(jobExplorer, JobExplorer.class);
  }

  private <T> T createLazyProxy(AtomicReference<T> reference, Class<T> type) {
    ProxyFactory factory = new ProxyFactory();
    factory.setTargetSource(new ReferenceTargetSource<>(reference));
    factory.addAdvice(new PassthruAdvice());
    factory.setInterfaces(new Class<?>[] { type });
    @SuppressWarnings("unchecked")
    T proxy = (T) factory.getProxy();
    return proxy;
  }
  
  @Override
  public void setImportMetadata(AnnotationMetadata importMetadata) {
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.jobBuilderFactory = new JobBuilderFactory(jobRepository());
    this.stepBuilderFactory = new StepBuilderFactory(jobRepository(), configurer.getTransactionManager());
  }

  protected void initialize() throws Exception {
	log.info("Initialize Batch Configuration");
    if (initialized) return;
    jobRepository.set(configurer.getJobRepository());
    jobLauncher.set(configurer.getJobLauncher());
    transactionManager.set(configurer.getTransactionManager());
    jobRegistry.set(new MapJobRegistry());
    jobExplorer.set(configurer.getJobExplorer());
    initialized = true;
  }

  private class PassthruAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
      return invocation.proceed();
    }

  }

  private class ReferenceTargetSource<T> extends AbstractLazyCreationTargetSource {

    private AtomicReference<T> reference;

    public ReferenceTargetSource(AtomicReference<T> reference) {
      this.reference = reference;
    }

    @Override
    protected Object createObject() throws Exception {
      initialize();
      return reference.get();
    }
  }
}

@Configuration(proxyBeanMethods = false)
class CustomScopeConfiguration {

  private static StepScope stepScope;

  private static JobScope jobScope;

  static {
    jobScope = new JobScope();
    jobScope.setAutoProxy(false);

    stepScope = new StepScope();
    stepScope.setAutoProxy(false);
  }

  @Bean
  public static StepScope stepScope() {
    return stepScope;
  }

  @Bean
  public static JobScope jobScope() {
    return jobScope;
  }
}
