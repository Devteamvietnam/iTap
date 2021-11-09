package com.devteam.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
})
public class DataConfig {
    @Bean("TestService")
    public DataInitService createTestDataService() {
        return new DataInitService();
    }
}
