package com.devteam.digital.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    private Long maxSize;

    private Long avatarMaxSize;

    private Path mac;

    private Path linux;

    private Path windows;

    public Path getPath(){
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith(AdminConstant.WIN)) {
            return windows;
        } else if(os.toLowerCase().startsWith(AdminConstant.MAC)){
            return mac;
        }
        return linux;
    }

    @Data
    public static class Path{

        private String path;

        private String avatar;
    }
}

