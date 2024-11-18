package com.menara.authentication.configuration.client;

import com.menara.authentication.common.constant.CookiesNameConstants;
import com.menara.authentication.configuration.record.AccessBadge;
import com.menara.authentication.security.jwt.ServerJWTStorage;
import feign.RequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(4)
@AllArgsConstructor
public class UserConfiguration {
    private AccessBadge accessBadge;
    private ServerJWTStorage jwt;

    @Bean
    public RequestInterceptor requestInterceptorUser(){
        return template -> template
                .header("Authorization", "bearer " + jwt.getAccessToken())
                .header(accessBadge.name(), accessBadge.value())
                .header("Cookie", CookiesNameConstants.TOKEN_FINGERPRINT + "=" + jwt.getFingerprint() + ";");
    }
}
