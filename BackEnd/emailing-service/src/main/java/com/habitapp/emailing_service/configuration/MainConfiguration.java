package com.internship_hiring_menara.emailing_service.configuration;

import com.internship_hiring_menara.emailing_service.configuration.record.AccessBadge;
import com.internship_hiring_menara.emailing_service.configuration.record.CredentialEmailingService;
import com.internship_hiring_menara.emailing_service.configuration.record.AccessTokenRsaPubKeyConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(1)
@EnableConfigurationProperties({CredentialEmailingService.class,
        AccessBadge.class,
        AccessTokenRsaPubKeyConfig.class})
public class MainConfiguration {
}
