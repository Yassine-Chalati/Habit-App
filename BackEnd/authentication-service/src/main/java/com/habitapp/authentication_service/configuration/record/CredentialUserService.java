package com.menara.authentication.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "credential.service.user")
public record CredentialUserService(String username, String password) {
}
