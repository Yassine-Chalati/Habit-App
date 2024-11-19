package com.menara.authentication.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "jwt.refresh.token.rsa")
public record RefreshTokenRsaKeysConfig(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
}
