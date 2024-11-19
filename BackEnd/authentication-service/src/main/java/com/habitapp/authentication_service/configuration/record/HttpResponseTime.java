package com.menara.authentication.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "http")
public record HttpResponseTime(long responseTimeWithoutDispatching, long responseTimeWithDispatching) {
}
