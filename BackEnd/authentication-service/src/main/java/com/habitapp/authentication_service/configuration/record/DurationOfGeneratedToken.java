package com.habitapp.authentication_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "duration.generated.token")
public record DurationOfGeneratedToken(int accountActivationToken, int passwordResetToken) {
}
