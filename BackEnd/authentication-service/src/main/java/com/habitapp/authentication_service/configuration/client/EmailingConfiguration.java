package com.habitapp.authentication_service.configuration.client;

import feign.RequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@AllArgsConstructor
@Order(3)
public class EmailingConfiguration {
    @Bean
    public RequestInterceptor requestInterceptorEmailing(){
        return template -> template
                .header("testheader", "testheadervalue");
    }
}
