package com.devteam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource("classpath:application.yml"),
        @PropertySource(value = "classpath:application-dev.yml")
})
public class GeneralConfig {

    @Value("${app.username")
    public  String USER_NAME;

    @Value("${app.errorlevel}")
    public String ERROR_LEVEL;

    @Value("${app.dummyDataEnabled}")
    public boolean DUMMY_DATA_ENABLED;

    @Value("${app.bypassAuthentication:false}")
    public boolean BYPASS_AUTHENTICATION;

}
