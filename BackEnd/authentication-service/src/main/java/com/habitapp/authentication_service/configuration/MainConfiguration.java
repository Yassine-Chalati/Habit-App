package com.menara.authentication.configuration;

import com.menara.authentication.configuration.record.*;
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
        AccessBadge.class,
        FrontEndURL.class,
        DurationOfGeneratedToken.class,
        UrlDelegateService.class,
        CredentialEmailingService.class,
        JwtClaim.class,
        CookiesAttributesValues.class,
        GoogleOAuth2Credential.class,
        DurationResendUrl.class,
        HttpResponseTime.class,
        SuperAdminCredential.class,
        CredentialUserService.class
})
public class MainConfiguration {
}
