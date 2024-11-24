package com.habitapp.config_service.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.habitapp.config_service.configuration.record.Credential;

@Configuration
@Order(1)
@EnableConfigurationProperties({Credential.class})
public class MainConfiguration {
}
