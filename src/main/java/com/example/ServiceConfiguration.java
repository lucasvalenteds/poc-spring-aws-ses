package com.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class ServiceConfiguration {

    @Configuration
    @Profile("local")
    @PropertySource({"classpath:application.properties", "classpath:application-local.properties"})
    static class Local {
    }

    @Configuration
    @Profile("dev")
    @PropertySource({"classpath:application.properties", "classpath:application-dev.properties"})
    static class Development {
    }
}