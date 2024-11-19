package com.internship_hiring_menara.config_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "credential")
public record Credential(String username, String password) {
}
