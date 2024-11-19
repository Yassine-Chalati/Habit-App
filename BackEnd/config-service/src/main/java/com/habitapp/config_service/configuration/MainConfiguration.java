package com.internship_hiring_menara.config_service.configuration;

import com.internship_hiring_menara.config_service.configuration.record.Credential;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(1)
@EnableConfigurationProperties({Credential.class})
public class MainConfiguration {
}
