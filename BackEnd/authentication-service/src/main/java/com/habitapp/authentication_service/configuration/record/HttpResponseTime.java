package com.habitapp.authentication_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "http")
public record HttpResponseTime(long responseTimeWithoutDispatching, long responseTimeWithDispatching) {
}
