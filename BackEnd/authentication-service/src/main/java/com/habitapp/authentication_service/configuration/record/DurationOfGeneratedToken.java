package com.menara.authentication.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "duration.generated.token")
public record DurationOfGeneratedToken(int accountActivationToken, int passwordResetToken) {
}
