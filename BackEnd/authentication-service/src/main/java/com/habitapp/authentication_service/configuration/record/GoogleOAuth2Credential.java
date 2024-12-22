package com.habitapp.authentication_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
public record GoogleOAuth2Credential(String clientId, String clientSecret) {
}
