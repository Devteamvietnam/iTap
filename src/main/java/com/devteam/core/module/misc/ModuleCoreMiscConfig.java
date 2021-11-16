package com.devteam.core.module.misc;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = { "com.devteam.core.module.misc" })
@EnableConfigurationProperties
@EnableTransactionManagement
public class ModuleCoreMiscConfig {
}