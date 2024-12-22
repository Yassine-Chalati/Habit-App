package com.habitapp.authentication_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "credential.service.profile")
public record CredentialProfileService(String username, String password) {
}
