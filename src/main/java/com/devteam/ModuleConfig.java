package com.devteam;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {
        "com.devteam.module.http",
        "com.devteam.module.customer"
   }
)
@EnableJpaRepositories(
        basePackages  = {
                "com.devteam.module.customer.repository"
        }
)
@EnableConfigurationProperties
public class ModuleConfig {
}