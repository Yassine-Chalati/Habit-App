package com.habitapp.profile_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "jwt.access.token.rsa")
public record AccessTokenRsaPubKeyConfig(RSAPublicKey rsaPublicKey) {
}
