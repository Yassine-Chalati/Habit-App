package com.habitapp.authentication_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cookies")
public record CookiesAttributesValues(String secure, String prefixSecure, String prefixHost, String path, String pathValue) {
}
