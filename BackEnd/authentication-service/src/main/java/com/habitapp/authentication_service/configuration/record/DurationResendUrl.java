package com.habitapp.authentication_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "duration.resend")
public record DurationResendUrl(long activationUrl,long resetPasswordUrl) {
}
