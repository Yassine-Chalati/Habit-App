package com.menara.authentication.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cookies")
public record CookiesAttributesValues(String secure, String prefixSecure, String prefixHost, String path, String pathValue) {
}
