package com.devteam;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {
        "com.devteam.module.http",
        "com.devteam.module.customer",
        "com.devteam.module.account",
        "com.devteam.module.storage",
        "com.devteam.module.common"
   }
)
@EnableJpaRepositories(
        basePackages  = {
                "com.devteam.module.customer.repository",
                "com.devteam.module.account.repository"
        }
)
@EnableConfigurationProperties
public class ModuleConfig {
}