package com.devteam;

import com.devteam.module.account.entity.Role;
import com.devteam.module.account.entity.User;
import com.devteam.module.account.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;

@SpringBootApplication
public class DigitalApplication{
    public static void main(String[] args) {
        SpringApplication.run(DigitalApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.saveRole(new Role("ROLE_USER"));
            userService.saveRole(new Role("ROLE_MANAGER"));
            userService.saveRole(new Role("ROLE_ADMIN"));
            userService.saveRole(new Role("ROLE_SUPER_ADMIN"));

            userService.saveUser(new User("ddthien", "devteamvietnam@gmail.com","admin","password1","https://yt3.ggpht.com/ytc/AKedOLSIiddxwkM9_HTPsxnmJneNfQwicvl18M9wXzcCVQ=s900-c-k-c0x00ffffff-no-rj",true));

            userService.addRoleToUser("admin", "ROLE_ADMIN");
        };
    }

}
