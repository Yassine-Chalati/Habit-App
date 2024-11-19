package com.habitapp.registry_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "credential")
public record Credential(String username, String password) {
}
