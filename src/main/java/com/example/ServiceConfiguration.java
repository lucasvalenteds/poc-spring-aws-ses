package com.example;

import com.example.email.SESPostman;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class ServiceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfiguration.class);

    public static final String LOCAL = "local";
    public static final String DEVELOPMENT = "dev";

    @Configuration
    @Profile(LOCAL)
    @PropertySource({"classpath:application.properties", "classpath:application-" + LOCAL + ".properties"})
    static class Local implements ApplicationListener<ContextRefreshedEvent> {

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            event.getApplicationContext()
                .getBean(SESPostman.class)
                .verifySourceEmail()
                .doOnError(throwable -> LOGGER.error("Could not verify source e-mail address", throwable))
                .doOnSuccess(it -> LOGGER.info("Source e-mail address verified successfully"))
                .block();
        }
    }

    @Configuration
    @Profile(DEVELOPMENT)
    @PropertySource({"classpath:application.properties", "classpath:application-" + DEVELOPMENT + ".properties"})
    static class Development {
    }
}