package com.habitapp.profile_service.configuration.client;

import com.habitapp.profile_service.common.CookiesNameConstants;
import com.habitapp.profile_service.configuration.record.AccessBadge;
import com.habitapp.profile_service.security.jwt.ServerJWTStorage;
import feign.RequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@AllArgsConstructor
@Order(3)
public class AuthenticationConfiguration {
    private AccessBadge accessBadge;
    private ServerJWTStorage jwt;

    @Bean
    public RequestInterceptor requestInterceptorEmailing(){
        return template -> template
                .header("Authorization", jwt.getAccessToken())
                .header(accessBadge.name(), accessBadge.value())
                .header("Cookie", CookiesNameConstants.TOKEN_FINGERPRINT + "=" + jwt.getFingerprint() + ";");
    }
}
