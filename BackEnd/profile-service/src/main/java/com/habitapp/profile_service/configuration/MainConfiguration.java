package com.habitapp.profile_service.configuration;

import com.habitapp.profile_service.configuration.record.AccessBadge;
import com.habitapp.profile_service.configuration.record.CredentialUserService;
import com.habitapp.profile_service.configuration.record.FrontEndURL;
import com.habitapp.profile_service.configuration.record.AccessTokenRsaPubKeyConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(1)
@EnableConfigurationProperties({AccessBadge.class,
        FrontEndURL.class,
        AccessTokenRsaPubKeyConfig.class,
        CredentialUserService.class
})
public class MainConfiguration {
}
