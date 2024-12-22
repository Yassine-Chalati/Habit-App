package com.habitapp.authentication_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.access.badge")
public record AccessBadge(String name, String value) {
}