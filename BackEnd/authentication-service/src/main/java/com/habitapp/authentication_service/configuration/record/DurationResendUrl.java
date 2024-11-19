package com.menara.authentication.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "duration.resend")
public record DurationResendUrl(long activationUrl,long resetPasswordUrl) {
}
