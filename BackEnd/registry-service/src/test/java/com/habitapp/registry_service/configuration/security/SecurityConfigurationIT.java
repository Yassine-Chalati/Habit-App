package com.habitapp.registry_service.configuration.security;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.habitapp.registry_service.configuration.record.Credential;

@SpringBootTest
@Import(SecurityConfiguration.class)
class SecurityConfigurationIT {

    @Autowired
    private Credential credential;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Test
    void testPasswordEncoderBean() {
        String rawPassword = "plainTextPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertThat(passwordEncoder.matches(rawPassword, encodedPassword))
                .isTrue();
    }

    @Test
    void testInMemoryUserDetailsManagerBean() {
        UserDetails user = inMemoryUserDetailsManager.loadUserByUsername(credential.username());
        assertThat(user).isNotNull();
        assertThat(passwordEncoder.matches(credential.password(), user.getPassword())).isTrue();
    }

    @Test
    void testSecurityFilterChainBean() {
        assertThat(securityFilterChain).isNotNull();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public Credential credential() {
            return new Credential("testUser", "testPassword");
            
        }
    }
}
