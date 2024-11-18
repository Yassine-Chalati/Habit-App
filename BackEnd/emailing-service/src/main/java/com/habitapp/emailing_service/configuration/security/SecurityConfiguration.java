package com.internship_hiring_menara.emailing_service.configuration.security;

import com.internship_hiring_menara.emailing_service.configuration.record.AccessTokenRsaPubKeyConfig;
import com.internship_hiring_menara.emailing_service.security.filter.VerifyAccessBadgeFilter;
import com.internship_hiring_menara.emailing_service.security.filter.VerifyRevokedJwtFilter;
import com.internship_hiring_menara.emailing_service.security.filter.VerifyTokenFingerprintFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
@Order(2)
public class SecurityConfiguration {
    private AccessTokenRsaPubKeyConfig accessTokenRsaPubKeyConfig;
    private VerifyAccessBadgeFilter verifyAccessBadgeFilter;
    private VerifyTokenFingerprintFilter verifyTokenFingerprintFilter;
    private VerifyRevokedJwtFilter verifyRevokedJwtFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
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
                .addFilterBefore(verifyAccessBadgeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(verifyTokenFingerprintFilter, BearerTokenAuthenticationFilter.class)
                .addFilterAfter(verifyRevokedJwtFilter, VerifyTokenFingerprintFilter.class)
                .build();
    }

    /*@Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
        return new InMemoryUserDetailsManager(User
                .withUsername(credential.username())
                .password(passwordEncoder.encode(credential.password()))
                .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }*/

    @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(accessTokenRsaPubKeyConfig.rsaPublicKey())
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
