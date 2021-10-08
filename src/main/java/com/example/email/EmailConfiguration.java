package com.example.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.ses.SesAsyncClient;

import java.util.List;

@Configuration
public class EmailConfiguration {

    @Bean
    Postman postman(List<Email> emails, SesAsyncClient client, String source) {
        return new SESPostman(emails, client, source);
    }
}
