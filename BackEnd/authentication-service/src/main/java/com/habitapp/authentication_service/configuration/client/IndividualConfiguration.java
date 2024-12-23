package com.habitapp.authentication_service.configuration.client;

import feign.RequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(4)
@AllArgsConstructor
public class IndividualConfiguration {

    @Bean
    public RequestInterceptor requestInterceptorUser(){
        return template -> template
                .header("testheader", "testheadervalue");
    }
}
