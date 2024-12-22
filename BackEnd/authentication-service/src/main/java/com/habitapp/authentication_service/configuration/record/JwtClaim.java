package com.habitapp.authentication_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt.claim")
public record JwtClaim(String issuer, long accessTokenExpirationTime, long refreshTokenExpirationTime, long idTokenExpirationTime, String audience, String clientId) {
}
