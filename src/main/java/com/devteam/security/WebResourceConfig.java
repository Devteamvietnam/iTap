package com.devteam.security;

import com.devteam.core.springframework.AppEnv;
import static springfox.documentation.builders.PathSelectors.regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

@Configuration
@EnableOpenApi
public class WebResourceConfig extends WebMvcConfigurationSupport {

    @Autowired
    AppEnv appEnv;

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.OAS_30).
                ignoredParameterTypes(HttpServletRequest.class, HttpSession.class).
                select().
                apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).
                paths(regex("/(rest/v1.0.0|storage|get)/.*")).
                build().
                apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder().
                title("DVFX REST API").
                description("DVFX REST API").
                version("1.0.0").
                license("DVFX License").
                licenseUrl("https://dev-demo.website").
                contact(new Contact("Devrean", "https://dev-demo.website", "devteamvietnam@gmail.com")).
                build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);

        Set<String> addons = appEnv.getAddons();
        String[] downloadPaths = new String[addons.size()] ;
        String[] publicPaths = new String[addons.size()] ;
        int idx = 0;
        for(String addon : addons) {
            downloadPaths[idx] = appEnv.fileResourcePath("addons/" + addon + "/www/download/");
            publicPaths[idx] = appEnv.fileResourcePath("addons/" + addon + "/www/public/");
            idx++;
        }

        registry
                .addResourceHandler("/download/**")
                .addResourceLocations(downloadPaths)
                .resourceChain(false);

        registry
                .addResourceHandler("/**")
                //.addResourceLocations("classpath:/public/");
                .addResourceLocations(publicPaths);
    }
}

