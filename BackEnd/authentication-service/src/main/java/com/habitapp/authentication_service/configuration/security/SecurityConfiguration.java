package com.habitapp.authentication_service.configuration.security;


import com.habitapp.authentication_service.annotation.Instance;
import com.habitapp.authentication_service.configuration.record.AccessTokenRsaKeysConfig;
import com.habitapp.authentication_service.configuration.record.RefreshTokenRsaKeysConfig;
import com.habitapp.authentication_service.domain.entity.DefaultAccountIndividual;
import com.habitapp.authentication_service.domain.entity.Permission;
import com.habitapp.authentication_service.domain.entity.Role;
import com.habitapp.authentication_service.domain.repository.DefaultAccountIndividualRepository;
import com.habitapp.authentication_service.security.userdetails.IndividualDefaultMethod;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Order(2)
@AllArgsConstructor
public class SecurityConfiguration  {
    private AccessTokenRsaKeysConfig accessTokenRsaKeysConfig;
    private RefreshTokenRsaKeysConfig refreshTokenRsaKeysConfig;
    private DefaultAccountIndividualRepository defaultAccountIndividualRepository;

    //todo transform filter to DI
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/authentication/individual/default",
                                "/authentication/refresh/token",
                                "/account/individual/default-method/create",
                                "/account/individual/default-method/activate/token/*",
                                "/swagger-ui/*",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    PasswordEncoder delegatingPasswordEncoder(){
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        String idForEncode = "bcrypt";


        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_5());
        encoders.put("pbkdf2@SpringSecurity_v5_8", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v4_1());
        encoders.put("scrypt@SpringSecurity_v5_8", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_2());
        encoders.put("argon2@SpringSecurity_v5_8", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("sha256", new StandardPasswordEncoder());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    @Bean
    @Primary
    @Instance("accessJwtEncoder")
    JwtEncoder accessJwtEncoder(){
        JWK jwk = new RSAKey.Builder(accessTokenRsaKeysConfig.rsaPublicKey()).privateKey(accessTokenRsaKeysConfig.rsaPrivateKey())
                .build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    @Primary
    @Instance("accessJwtDecoder")
    JwtDecoder accessJwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(accessTokenRsaKeysConfig.rsaPublicKey())
                .build();
    }

    @Bean
    @Instance("refreshJwtEncoder")
    JwtEncoder refreshJwtEncoder(){
        JWK jwk = new RSAKey.Builder(refreshTokenRsaKeysConfig.rsaPublicKey()).privateKey(refreshTokenRsaKeysConfig.rsaPrivateKey())
                .build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    @Instance("refreshJwtDecoder")
    JwtDecoder refreshJwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(refreshTokenRsaKeysConfig.rsaPublicKey())
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

    // User details service objects for refresh token authentication
    @Bean
    @Instance("RefreshTokenDefaultAccountIndividualDetails")
    public UserDetailsService userDetailsServiceDefaultAccountIndividualRefreshToken() {
        return id -> {
            DefaultAccountIndividual defaultAccountIndividual;
            Optional<DefaultAccountIndividual> optional = defaultAccountIndividualRepository.findById(Long.parseLong(id));
            String[] roles;
            String[] permissions;

            if(optional.isPresent()) {
                defaultAccountIndividual = optional.get();
            } else {
                throw new UsernameNotFoundException("User not found Exception");
            }

            roles = this.convertRolesToListString(defaultAccountIndividual.getRoles());
            permissions = this.convertPermissionsToListString(defaultAccountIndividual.getPermissions());

            return IndividualDefaultMethod.builder()
                    .activated(defaultAccountIndividual.isActivated())
                    .email(defaultAccountIndividual.getEmail())
                    .id(defaultAccountIndividual.getId())
                    .roles(roles)
                    .permissions(permissions)
                    .build();
        };
    }
    // User details service objects for simple authentication

    @Bean
    @Instance("DefaultAccountIndividualDetails")
    public UserDetailsService userDetailsServiceDefaultAccountIndividual() {
        return email -> {
            DefaultAccountIndividual defaultAccountIndividual = defaultAccountIndividualRepository.findDefaultAccountIndividualByEmail(email);
            String[] roles;
            String[] permissions;

            if(defaultAccountIndividual == null) {
                throw new UsernameNotFoundException("User not found Exception");
            }

            roles = this.convertRolesToListString(defaultAccountIndividual.getRoles());
            permissions = this.convertPermissionsToListString(defaultAccountIndividual.getPermissions());

            return IndividualDefaultMethod.builder()
                    .activated(defaultAccountIndividual.isActivated())
                    .email(defaultAccountIndividual.getEmail())
                    .id(defaultAccountIndividual.getId())
                    .password(defaultAccountIndividual.getPassword())
                    .roles(roles)
                    .permissions(permissions)
                    .build();
        };
    }

    // Authentication Manager objects

    @Bean
    @Instance("DefaultAccountIndividual")
    @Primary
    public AuthenticationManager authenticationManagerDefaultAccountIndividual(@Instance("DefaultAccountIndividualDetails") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        // DaoAuthenticationProvider is used because our authentication is performing by providing just USERNAME/PASSWORD
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    @Instance("RefreshToken")
    public AuthenticationManager authenticationManagerRefreshToken(@Instance("refreshJwtDecoder") JwtDecoder refreshJwtDecoder, JwtAuthenticationConverter jwtAuthenticationConverter){
        // DaoAuthenticationProvider is used because our authentication is performing by providing just USERNAME/PASSWORD
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        return new ProviderManager(daoAuthenticationProvider);

        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(refreshJwtDecoder);
        jwtAuthenticationProvider.setJwtAuthenticationConverter(jwtAuthenticationConverter);
        return new ProviderManager(jwtAuthenticationProvider);
    }


    // internal methods of some internal process in this class

    private String[] convertRolesToListString(List<Role> roleList){
        String[] roles = new String[roleList.size()];

        for (int i = 0; i < roleList.size(); i++ ){
            roles[i] = roleList.get(i).getRole();
        }
        return roles;
    }

    private String[] convertPermissionsToListString(List<Permission> permissionList){
        String[] permissions = new String[permissionList.size()];

        for (int i = 0; i < permissionList.size(); i++ ){
            permissions[i] = permissionList.get(i).getPermission();
        }
        return permissions;
    }
}
