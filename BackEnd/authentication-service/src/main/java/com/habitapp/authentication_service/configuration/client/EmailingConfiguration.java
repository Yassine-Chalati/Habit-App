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
@AllArgsConstructor
@Order(3)
public class EmailingConfiguration {
//    private CredentialEmailingService credential;
    private AccessBadge accessBadge;
    private ServerJWTStorage jwt;

//    @Bean
//    public Contract feignContract(){
//        return new feign.Contract.Default();
//    }

//    @Bean
//    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(){
//        return new BasicAuthRequestInterceptor(credential.username(), credential.password());
//    }

//    @Bean
//    public ErrorDecoder errorDecoder(){
//        return new CustomErrorDecoder();
//    }

    @Bean
    public RequestInterceptor requestInterceptorEmailing(){
        return template -> template
                .header("Authorization", "bearer " + jwt.getAccessToken())
                .header(accessBadge.name(), accessBadge.value())
                .header("Cookie", CookiesNameConstants.TOKEN_FINGERPRINT + "=" + jwt.getFingerprint() + ";");
    }
}
