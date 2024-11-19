package com.menara.authentication.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "credential.service.emailing")
public record CredentialEmailingService(String username, String password) {
}
