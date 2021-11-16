package com.devteam.core;

import com.devteam.config.JpaConfiguration;
import com.devteam.core.data.repository.DataTPRepositoryFactoryBean;
import com.devteam.core.springframework.ModuleConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ModuleConfig(
        basePackages = {
                "com.devteam.core.data.db",
                "com.devteam.core.http",
                "com.devteam.core.springframework"
        }
)
@Configuration
@ComponentScan(basePackages = {
        "com.devteam.core.data.*",
        "com.devteam.core.http.*",
        }
)
@EnableConfigurationProperties
@EnableTransactionManagement
@EnableJpaRepositories(
        repositoryFactoryBeanClass = DataTPRepositoryFactoryBean.class
)
@EnableAutoConfiguration
@Import({JpaConfiguration.class})
public class CoreDataModuleConfig {
}
