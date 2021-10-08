package com.example.user;

import com.example.email.Email;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;

import java.nio.file.Path;

@Configuration
@EnableWebFlux
public class UserConfiguration {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    Email userCreatedEmail(ISpringWebFluxTemplateEngine templateEngine, Path templatesDirectory) {
        return new UserCreatedEmail(templateEngine, templatesDirectory);
    }

    @Bean
    Email userDeletedEmail(ISpringWebFluxTemplateEngine templateEngine, Path templatesDirectory) {
        return new UserDeletedEmail(templateEngine, templatesDirectory);
    }
}
