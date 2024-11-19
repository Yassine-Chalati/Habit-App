package com.internship_hiring_menara.gateway_service.configuration;

import com.internship_hiring_menara.gateway_service.configuration.record.AccessBadge;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@EnableConfigurationProperties({AccessBadge.class})
@Order(1)
public class MainConfiguration {
}
