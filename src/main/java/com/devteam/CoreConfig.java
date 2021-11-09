package com.devteam;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {
        "com.devteam.core",
        "com.devteam.core.plugin"
}
)
@EnableJpaRepositories(
        basePackages  = {
                "com.devteam.core.plugin.repository"
        }
)
@EnableConfigurationProperties
public class CoreConfig {
}
