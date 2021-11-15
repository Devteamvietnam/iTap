package com.devteam.core.data;

import com.devteam.config.JpaConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {
        "com.devteam.core.data.db",
}
)
@EnableConfigurationProperties
@EnableTransactionManagement
@EnableJpaRepositories(
)
@EnableAutoConfiguration
@Import({JpaConfiguration.class})
public class CoreDataModuleConfig {
}
