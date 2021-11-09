package com.devteam.service;

import com.devteam.config.ClientInfo;
import com.devteam.data.DBService;
import com.devteam.data.sample.EntityDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.devteam.util.text.DateUtil;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataInitService {
    @Getter
    @Autowired
    private ApplicationContext context;

    @Autowired
    private DBService dbService ;

    @Autowired

    @Value("${app.data.init-sample:false}")
    private boolean initSample;

    @Order(1)
    @EventListener({ ContextRefreshedEvent.class })
    void onContextRefreshedEvent() {
        runInitData(ClientInfo.DEFAULT);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void runInitData(ClientInfo client) {
        log.info("Start Data Import");
        long start = System.currentTimeMillis();
        try {
                EntityDB.initDataDB(context);
                dbService.initDb(client, initSample);
                EntityDB.clearDataDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long execTime = System.currentTimeMillis() - start;
        System.out.println("Import in " + (DateUtil.asHumanReadable(execTime)));
    }
}
