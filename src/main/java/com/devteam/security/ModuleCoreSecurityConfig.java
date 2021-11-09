package com.devteam.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"com.devteam.security"})
@EnableJpaRepositories(
  basePackages = {"com.devteam.security.repository"}
  )
@EnableConfigurationProperties
@EnableTransactionManagement

public class ModuleCoreSecurityConfig {
}