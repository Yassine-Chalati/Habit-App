package com.habitapp.authentication_service.configuration;


import com.habitapp.authentication_service.configuration.record.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Order(1)
@EnableScheduling
@EnableConfigurationProperties({
        AccessTokenRsaKeysConfig.class,
        RefreshTokenRsaKeysConfig.class,
        DurationOfGeneratedToken.class,
        JwtClaim.class,
        DurationResendUrl.class,
        HttpResponseTime.class,
        UrlDelegateService.class
})
public class MainConfiguration {
}
