package com.habitapp.authentication_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "jwt.access.token.rsa")
public record AccessTokenRsaKeysConfig(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
}
