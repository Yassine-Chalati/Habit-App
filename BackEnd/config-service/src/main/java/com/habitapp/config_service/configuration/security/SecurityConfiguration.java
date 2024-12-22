package com.habitapp.config_service.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.habitapp.config_service.configuration.record.Credential;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@Order(2)
@AllArgsConstructor
public class SecurityConfiguration {

    private Credential credential;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }

    /**
     * @param passwordEncoder is for hashing password using BCrypt
     * @return {@return } it contains the User information used for authenticate
     * to config client
     */
    @Bean
    InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(User.builder()
                .username(this.credential.username())
                .password(passwordEncoder.encode(credential.password()))
                .build()
        );
    }

    @Bean
    PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
