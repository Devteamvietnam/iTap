package com.devteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication (
        scanBasePackages= {
                "com.devteam.*"
        },
        exclude = {
        SecurityAutoConfiguration.class
})
@EnableConfigurationProperties
@Import(value = {
        ModuleConfig.class
})
public class DigitalApplication{
    public static void main(String[] args) {
        SpringApplication.run(DigitalApplication.class, args);
    }
}
