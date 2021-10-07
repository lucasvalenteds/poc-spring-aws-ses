package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesAsyncClient;

import java.net.URI;

@Configuration
public class SESConfiguration {

    @Bean
    AwsCredentialsProvider credentialsProvider(Environment environment) {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(
            environment.getRequiredProperty("aws.accessKey", String.class),
            environment.getRequiredProperty("aws.secretKey", String.class)
        ));
    }

    @Bean
    URI endpoint(Environment environment) {
        return URI.create(environment.getRequiredProperty("aws.url", String.class));
    }

    @Bean
    Region region(Environment environment) {
        return Region.of(environment.getRequiredProperty("aws.region", String.class));
    }

    @Bean
    SesAsyncClient sesClient(AwsCredentialsProvider credentialsProvider, URI endpoint, Region region) {
        return SesAsyncClient.builder()
            .credentialsProvider(credentialsProvider)
            .endpointOverride(endpoint)
            .region(region)
            .build();
    }

    @Bean
    String source(Environment environment) {
        return environment.getRequiredProperty("aws.ses.source", String.class);
    }
}
