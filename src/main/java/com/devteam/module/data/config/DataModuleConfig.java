package com.devteam.module.data.config;

import com.devteam.core.module.http.config.CoreHttpModuleConfig;
import com.devteam.core.module.srpingframework.config.ModuleConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ModuleConfig(
        basePackages= { "com.devteam.module.data" }
)
@Configuration
@ComponentScan(basePackages = {
        "com.devteam.module.data"
}
)
@EnableJpaRepositories(
        basePackages = { "com.devteam.module.data.repository" }
)
@Import({CoreHttpModuleConfig.class})
@EnableConfigurationProperties
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class DataModuleConfig {
}
