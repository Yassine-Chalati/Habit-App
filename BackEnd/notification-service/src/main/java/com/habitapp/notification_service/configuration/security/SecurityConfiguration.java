package com.habitapp.notification_service.configuration.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
@Order(2)
public class SecurityConfiguration {
    // private FrontEndURL frontEndURL;
    // private AccessTokenRsaPubKeyConfig accessTokenRsaPubKeyConfig;
    // private VerifyAccessBadgeFilter verifyAccessBadgeFilter;
    // private VerifyTokenFingerprintFilter verifyTokenFingerprintFilter;
    // private VerifyRevokedJwtFilter verifyRevokedJwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults()))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                // .addFilterBefore(verifyAccessBadgeFilter, DisableEncodeUrlFilter.class)
                // .addFilterAfter(verifyTokenFingerprintFilter, BearerTokenAuthenticationFilter.class)
                // .addFilterAfter(verifyRevokedJwtFilter, VerifyTokenFingerprintFilter.class)
                .build();
    }

    // @Bean
    // CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration configuration = new CorsConfiguration();
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    //     configuration.setAllowedOrigins(Collections.singletonList(this.frontEndURL.url()));
    //     configuration.setAllowedMethods(Arrays.asList("GET","POST"));
    //     source.registerCorsConfiguration("/**", configuration);
    //     return source;
    // }

    // @Bean
    // JwtDecoder jwtDecoder(){
    //     return NimbusJwtDecoder.withPublicKey(accessTokenRsaPubKeyConfig.rsaPublicKey())
    //             .build();
    // }

    // @Bean
    // public JwtAuthenticationConverter jwtAuthenticationConverter(){
    //     JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    //     grantedAuthoritiesConverter.setAuthorityPrefix("");

    //     JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    //     jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
    //     return jwtAuthenticationConverter;
    // }

}
