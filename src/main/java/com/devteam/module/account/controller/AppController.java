package com.devteam.module.account.controller;

import javax.annotation.PostConstruct;

import com.devteam.config.GeneralConfig;
import com.devteam.module.account.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
public class AppController {

    private static final Logger log = LoggerFactory.getLogger(AppController.class);

    @Autowired
    GeneralConfig config;
    @Autowired
    UserService userService;

    @PostConstruct
    public void init() throws JsonProcessingException {
        log.info("Initialize data");
        //initialize data here
        userService.init();
    }


}
