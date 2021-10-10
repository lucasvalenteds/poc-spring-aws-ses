package com.example.testing;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:application.properties", "classpath:application-local.properties"})
public class TestingConfiguration {
}
