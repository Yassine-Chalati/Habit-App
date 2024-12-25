package com.habitapp.progress_service.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

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
                .permitAll()
                )
                .headers((httpSecurityHeadersConfigurer -> {
                    httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
                }))
                .sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // .oauth2ResourceServer(oauth2 -> oauth2
                // .jwt(Customizer.withDefaults()))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // .addFilterBefore(verifyAccessBadgeFilter, DisableEncodeUrlFilter.class)
                // .addFilterAfter(verifyTokenFingerprintFilter, BearerTokenAuthenticationFilter.class)
                // .addFilterAfter(verifyRevokedJwtFilter, VerifyTokenFingerprintFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://77.37.86.136:4200", "http://77.37.86.136:8002"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
