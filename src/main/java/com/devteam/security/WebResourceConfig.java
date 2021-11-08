package com.devteam.security;

import com.devteam.util.IOUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableOpenApi
public class WebResourceConfig extends WebMvcConfigurationSupport {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.OAS_30).
                ignoredParameterTypes(HttpServletRequest.class, HttpSession.class).
                select().
                apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).
                paths(regex("/(rest/dev/v1|storage|get)/.*")).
                build().
                apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder().
                title("DEV TEAM REST API").
                description("DEV TEAM REST API").
                version("1.0.0").
                license("DEV TEAM License").
                licenseUrl("https://dev-demo.website").
                contact(new Contact("Dev Team", "https://dev-demo.website", "devteamvietnam@gmail.com")).
                build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);

        registry
                .addResourceHandler("/").addResourceLocations("classpath:/public/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry
                .addViewController("/swagger-ui/")
                .setViewName("forward:/swagger-ui/index.html");
    }

}
