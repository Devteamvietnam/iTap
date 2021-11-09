package com.devteam.module.storage;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class AppEnv {
    @Getter
    @Value("${app.home")
    private String appHome;

    @Getter
    @Value("${app.config.dir")
    private String appConfigDir;

    @Getter
    @Value("${app.upload.dir")
    private String uploadDir;

    @Getter
    @Value("${app.storage.dir}")
    private String storageDir ;

    @PostConstruct
    public void onInit() throws Exception {
        if(appConfigDir == null) {
            appConfigDir = appHome;
        }
        if(uploadDir == null) {
            uploadDir = appHome;
        }

        if(storageDir == null) {
            storageDir = appHome;
        }
    }


    public String fileResourcePath(String relativePath) {
        if(appHome.startsWith("/")) {
            return "file:" + appHome + "/" + relativePath;
        }
        return "file:/" + appHome + "/" + relativePath;
    }

    public String filePath(String relativePath) {
        return appHome + "/" + relativePath;
    }
}
