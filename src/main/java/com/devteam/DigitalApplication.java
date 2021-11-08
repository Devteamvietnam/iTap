package com.devteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.devteam.*")
public class DigitalApplication{
    public static void main(String[] args) {
        SpringApplication.run(DigitalApplication.class, args);
    }
}
