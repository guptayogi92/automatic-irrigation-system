package com.automation.irrigationsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

@Configuration
@EnableSwagger2
//@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.automation.irrigationsystem.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .produces(produceAndConsume())
                .consumes(produceAndConsume());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Automatic Irrigation API",
                "The purpose of this document is to introduce Automatic irrigation API.\n This is a technical document  ",
                "1.0.0",
                "Terms of service",
                new Contact("Automatic Irrigation", "https://www.google.com", "yogesh.gupta01@nagarro.com"),
                "License of API", "API license URL", new ArrayList<>());
    }

    private Set<String> produceAndConsume(){
        return new HashSet<>(Arrays.asList("application/json"));
    }
}