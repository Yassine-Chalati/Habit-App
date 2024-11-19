package com.menara.authentication.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "credential.account.admin.super")
public record SuperAdminCredential(String username, String password) {
}
