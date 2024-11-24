package com.habitapp.config_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "credential")
public record Credential(String username, String password) {
}
