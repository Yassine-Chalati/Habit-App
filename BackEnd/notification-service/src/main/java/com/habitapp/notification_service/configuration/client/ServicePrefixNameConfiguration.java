package com.habitapp.notification_service.configuration.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import feign.RequestInterceptor;
import lombok.AllArgsConstructor;

@Configuration
@Order(4)
@AllArgsConstructor
public class ServicePrefixNameConfiguration {
    @Bean
    public RequestInterceptor requestInterceptorUser(){
        return template -> template
                .header("header name and its value");
    }
}
