package com.devteam;

import com.devteam.config.JpaConfiguration;
import com.devteam.core.DBService;
import com.devteam.core.sample.EntityDB;
import com.devteam.module.account.service.AccountService;
import com.devteam.module.common.ClientInfo;
import com.devteam.module.common.ServiceMethodCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment= SpringBootTest.WebEnvironment.NONE,
        classes = { ModuleConfig.class, CoreConfig.class },
        properties = {
                "spring.config.location=classpath:application-test.yaml"
        }
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
abstract public class TestConfig {

    @Autowired
    ApplicationContext context;

    @Autowired
    protected DBService dbService ;

    @BeforeEach
    public void clearDataDB() throws Exception {
        EntityDB.initDataDB(context);
    }
}
