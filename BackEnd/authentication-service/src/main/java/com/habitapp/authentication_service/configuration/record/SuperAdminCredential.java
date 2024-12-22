package com.habitapp.authentication_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "credential.account.admin.super")
public record SuperAdminCredential(String username, String password) {
}
