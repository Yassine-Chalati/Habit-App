package com.habitapp.habit_service.configuration.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import feign.RequestInterceptor;

@Configuration
@Order(4)
// @AllArgsConstructor
public class NotificationConfiguration {
    // @Bean
    public RequestInterceptor requestInterceptorUser(){
        return template -> template
                .header("header name and its value");
    }
}
