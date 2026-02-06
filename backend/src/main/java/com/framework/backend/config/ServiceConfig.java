package com.framework.backend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = "com.framework.backend.service")
public class ServiceConfig {
    // Configuration pour scanner les beans de service

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}