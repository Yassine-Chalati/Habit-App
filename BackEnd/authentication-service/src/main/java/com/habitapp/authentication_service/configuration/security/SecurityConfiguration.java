package com.menara.authentication.configuration.security;

import com.internship_hiring_menara.common.common.account.PermissionNameCommonConstants;
import com.internship_hiring_menara.common.common.account.RoleNameCommonConstants;
import com.internship_hiring_menara.common.common.account.ServiceIdCommonConstants;
import com.menara.authentication.annotation.Instance;
import com.menara.authentication.common.constant.SuperAdminIdAccountConstants;
import com.menara.authentication.configuration.record.*;
import com.menara.authentication.domain.entity.*;
import com.menara.authentication.domain.repository.DefaultAccountAdminRepository;
import com.menara.authentication.domain.repository.DefaultAccountCandidateRepository;
import com.menara.authentication.domain.repository.GoogleAccountCandidateRepository;
import com.menara.authentication.security.exception.userdetailsservice.IdAccountNotFoundException;
import com.menara.authentication.security.exception.userdetailsservice.PasswordNullException;
import com.menara.authentication.security.exception.userdetailsservice.UsernameNullException;
import com.menara.authentication.security.filter.VerifyAccessBadgeFilter;
import com.menara.authentication.security.filter.VerifyRevokedJwtFilter;
import com.menara.authentication.security.filter.VerifyTokenFingerprintFilter;
import com.menara.authentication.security.userdetails.*;
import com.menara.authentication.security.userdetailsservice.InMemoryUserDetailsManagerOfServiceDefaultMethod;
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
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
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
    private DefaultAccountCandidateRepository defaultAccountCandidateRepository;
    private GoogleAccountCandidateRepository googleAccountCandidateRepository;
    private FrontEndURL frontEndURL;
    private CredentialEmailingService credentialEmailingService;
    private CredentialUserService credentialUserService;
    private VerifyRevokedJwtFilter verifyRevokedJwtFilter;
    private VerifyTokenFingerprintFilter verifyTokenFingerprintFilter;
    private VerifyAccessBadgeFilter verifyAccessBadgeFilter;
    private SuperAdminCredential superAdminCredential;
    private DefaultAccountAdminRepository defaultAccountAdminRepository;

    //todo transform filter to DI
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/authentication/super-admin/default",
                                "/authentication/admin/default",
                                "/authentication/candidate/default",
                                "/authentication/candidate/google",
                                "/authentication/candidate/google/callback",
                                "/authentication/service/default",
                                "/authentication/refresh/token",
                                "/account/candidate/default-method/create",
                                "/account/candidate/default-method/activate/token/generate",
                                "/account/candidate/default-method/activate/token/*",
                                "/account/candidate/default-method/reset-password/token/generate",
                                "/account/candidate/default-method/reset-password/token/*").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults()))
                .cors(Customizer.withDefaults())
                .addFilterBefore(verifyAccessBadgeFilter, DisableEncodeUrlFilter.class) //BearerTokenAuthenticationFilter.class
                .addFilterAfter(verifyTokenFingerprintFilter, BearerTokenAuthenticationFilter.class)
                .addFilterAfter(verifyRevokedJwtFilter, VerifyTokenFingerprintFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        configuration.setAllowedOrigins(Collections.singletonList(this.frontEndURL.url()));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");
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
    @Instance("RefreshTokenDefaultAccountSuperAdminDetails")
    public UserDetailsService userDetailsServiceDefaultAccountSuperAdminRefreshToken() {
        return id -> {

            if(SuperAdminIdAccountConstants.SUPER_ADMIN_ID_ACCOUNT == null
                    || SuperAdminIdAccountConstants.SUPER_ADMIN_ID_ACCOUNT.isEmpty()
                    || SuperAdminIdAccountConstants.SUPER_ADMIN_ID_ACCOUNT.isBlank()){
                throw new IdAccountNotFoundException("idAccount is null");
            }

            if (!id.equals(SuperAdminIdAccountConstants.SUPER_ADMIN_ID_ACCOUNT)){
                throw new UsernameNotFoundException("the id not found");
            }

            String[] roles;
            String[] permissions;

            roles = RoleNameCommonConstants.allAdminsAndSuperAdminRoles.toArray(new String[0]);
            permissions = PermissionNameCommonConstants.allAdminsAndSuperAdminPermissions.toArray(new String[0]);

            return SuperAdminDefaultMethod.builder()
                    .id(SuperAdminIdAccountConstants.SUPER_ADMIN_ID_ACCOUNT)
                    .username(superAdminCredential.username())
                    .roles(roles)
                    .permissions(permissions)
                    .build();
        };
    }

    @Bean
    @Instance("RefreshTokenDefaultAccountAdminDetails")
    public UserDetailsService userDetailsServiceDefaultAccountAdminRefreshToken() {
        return id -> {
            DefaultAccountAdmin defaultAccountAdmin;
            Optional<DefaultAccountAdmin> optional = defaultAccountAdminRepository.findById(Long.parseLong(id));
            String[] roles;
            String[] permissions;

            if(optional.isPresent()) {
                defaultAccountAdmin = optional.get();
            } else {
                throw new UsernameNotFoundException("User not found Exception");
            }

            roles = this.convertRolesToListString(defaultAccountAdmin.getRoles());
            permissions = this.convertPermissionsToListString(defaultAccountAdmin.getPermissions());

            return AdminDefaultMethod.builder()
                    .suspended(defaultAccountAdmin.isSuspended())
                    .id(defaultAccountAdmin.getId())
                    .email(defaultAccountAdmin.getEmail())
                    .roles(roles)
                    .permissions(permissions)
                    .build();
        };
    }

    @Bean
    @Instance("RefreshTokenDefaultAccountCandidateDetails")
    public UserDetailsService userDetailsServiceDefaultAccountCandidateRefreshToken() {
        return id -> {
            DefaultAccountCandidate defaultAccountCandidate;
            Optional<DefaultAccountCandidate> optional = defaultAccountCandidateRepository.findById(Long.parseLong(id));
            String[] roles;
            String[] permissions;

            if(optional.isPresent()) {
                defaultAccountCandidate = optional.get();
            } else {
                throw new UsernameNotFoundException("User not found Exception");
            }

            roles = this.convertRolesToListString(defaultAccountCandidate.getRoles());
            permissions = this.convertPermissionsToListString(defaultAccountCandidate.getPermissions());

            return CandidateDefaultMethod.builder()
                    .activated(defaultAccountCandidate.isActivated())
                    .email(defaultAccountCandidate.getEmail())
                    .id(defaultAccountCandidate.getId())
                    .roles(roles)
                    .permissions(permissions)
                    .build();
        };
    }

    @Bean
    @Instance("RefreshTokenGoogleAccountCandidateDetails")
    public UserDetailsService userDetailsServiceGoogleAccountCandidateRefreshToken() {
        return id -> {
            GoogleAccountCandidate googleAccountCandidate;
            Optional<GoogleAccountCandidate> optional = googleAccountCandidateRepository.findById(Long.parseLong(id));
            String[] roles;
            String[] permissions;

            if (optional.isPresent()){
                googleAccountCandidate = optional.get();
            } else {
                throw new UsernameNotFoundException("User not found Exception");
            }

            roles = this.convertRolesToListString(googleAccountCandidate.getRoles());
            permissions = this.convertPermissionsToListString(googleAccountCandidate.getPermissions());

            return CandidateGoogleMethod.builder()
                    .email(googleAccountCandidate.getEmail())
                    .id(googleAccountCandidate.getId())
                    .roles(roles)
                    .permissions(permissions)
                    .build();
        };
    }

    // User details service objects for simple authentication

    @Bean
    @Instance("DefaultAccountServiceDetails")
    public UserDetailsService userDetailsServiceDefaultAccountService(PasswordEncoder passwordEncoder){
        ServiceDefaultMethod emailingService;
        ServiceDefaultMethod userService;

        emailingService = ServiceDefaultMethod.builder()
                .id(ServiceIdCommonConstants.EMAILING_SERVICE)
                .username(credentialEmailingService.username())
                .password(passwordEncoder.encode(credentialEmailingService.password()))
                .roles(RoleNameCommonConstants.SERVER, RoleNameCommonConstants.EMAILING_SERVER)
                .permissions()
                .build();

        userService = ServiceDefaultMethod.builder()
                .id(ServiceIdCommonConstants.USER_SERVICE)
                .username(credentialUserService.username())
                .password(passwordEncoder.encode(credentialUserService.password()))
                .roles(RoleNameCommonConstants.SERVER, RoleNameCommonConstants.USER_SERVER)
                .permissions()
                .build();

        return new InMemoryUserDetailsManagerOfServiceDefaultMethod(
                emailingService,
                userService
        );
    }

    @Bean
    @Instance("DefaultAccountSuperAdminDetails")
    public UserDetailsService userDetailsServiceDefaultAccountSuperAdmin(PasswordEncoder passwordEncoder) {
        return username -> {

            if(superAdminCredential.username() == null
                    || superAdminCredential.username().isEmpty()
                    || superAdminCredential.username().isBlank()){
                throw new UsernameNullException("the super admin username is null");
            }

            if(superAdminCredential.password() == null
                    || superAdminCredential.password().isBlank()
                    || superAdminCredential.password().isEmpty()){
                throw new PasswordNullException("the super admin password is null");
            }

            if(SuperAdminIdAccountConstants.SUPER_ADMIN_ID_ACCOUNT == null
                    || SuperAdminIdAccountConstants.SUPER_ADMIN_ID_ACCOUNT.isEmpty()
                    || SuperAdminIdAccountConstants.SUPER_ADMIN_ID_ACCOUNT.isBlank()){
                throw new IdAccountNotFoundException("idAccount is null");
            }

            if (!username.equals(superAdminCredential.username())){
                throw new UsernameNotFoundException("the username not found");
            }

            String[] roles;
            String[] permissions;

            roles = RoleNameCommonConstants.allAdminsAndSuperAdminRoles.toArray(new String[0]);
            permissions = PermissionNameCommonConstants.allAdminsAndSuperAdminPermissions.toArray(new String[0]);

            return SuperAdminDefaultMethod.builder()
                    .id(SuperAdminIdAccountConstants.SUPER_ADMIN_ID_ACCOUNT)
                    .username(superAdminCredential.username())
                    .password(passwordEncoder.encode(superAdminCredential.password()))
                    .roles(roles)
                    .permissions(permissions)
                    .build();
        };
    }

    @Bean
    @Instance("DefaultAccountAdminDetails")
    public UserDetailsService userDetailsServiceDefaultAccountAdmin() {
        return email -> {
            DefaultAccountAdmin defaultAccountAdmin = defaultAccountAdminRepository.findDefaultAccountAdminByEmail(email);
            String[] roles;
            String[] permissions;

            if(defaultAccountAdmin == null)
                throw new UsernameNotFoundException("User not found Exception");

            roles = this.convertRolesToListString(defaultAccountAdmin.getRoles());
            permissions = this.convertPermissionsToListString(defaultAccountAdmin.getPermissions());

            return AdminDefaultMethod.builder()
                    .suspended(defaultAccountAdmin.isSuspended())
                    .id(defaultAccountAdmin.getId())
                    .email(defaultAccountAdmin.getEmail())
                    .password(defaultAccountAdmin.getPassword())
                    .roles(roles)
                    .permissions(permissions)
                    .build();
        };
    }

    @Bean
    @Instance("DefaultAccountCandidateDetails")
    public UserDetailsService userDetailsServiceDefaultAccountCandidate() {
        return email -> {
            DefaultAccountCandidate defaultAccountCandidate = defaultAccountCandidateRepository.findDefaultAccountCandidateByEmail(email);
            String[] roles;
            String[] permissions;

            if(defaultAccountCandidate == null) {
                throw new UsernameNotFoundException("User not found Exception");
            }

            roles = this.convertRolesToListString(defaultAccountCandidate.getRoles());
            permissions = this.convertPermissionsToListString(defaultAccountCandidate.getPermissions());

            return CandidateDefaultMethod.builder()
                    .activated(defaultAccountCandidate.isActivated())
                    .email(defaultAccountCandidate.getEmail())
                    .id(defaultAccountCandidate.getId())
                    .password(defaultAccountCandidate.getPassword())
                    .roles(roles)
                    .permissions(permissions)
                    .build();
        };
    }

    @Bean
    @Instance("GoogleAccountCandidateDetails")
    public UserDetailsService userDetailsServiceGoogleAccountCandidate() {
        return email -> {
            GoogleAccountCandidate googleAccountCandidate = googleAccountCandidateRepository.findGoogleAccountCandidateByEmail(email);
            String[] roles;
            String[] permissions;

            if(googleAccountCandidate == null) {
                throw new UsernameNotFoundException("User not found Exception");
            }

            roles = this.convertRolesToListString(googleAccountCandidate.getRoles());
            permissions = this.convertPermissionsToListString(googleAccountCandidate.getPermissions());

            return CandidateGoogleMethod.builder()
                    .email(googleAccountCandidate.getEmail())
                    .id(googleAccountCandidate.getId())
                    .roles(roles)
                    .permissions(permissions)
                    .build();
        };
    }

    // Authentication Manager objects

    @Bean
    @Instance("DefaultAccountService")
    public AuthenticationManager authenticationManagerDefaultAccountService(@Instance("DefaultAccountServiceDetails") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    @Instance("DefaultAccountSuperAdmin")
    public AuthenticationManager authenticationManagerDefaultAccountSuperAdmin(@Instance("DefaultAccountSuperAdminDetails") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        // DaoAuthenticationProvider is used because our authentication is performing by providing just USERNAME/PASSWORD
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    @Instance("DefaultAccountAdmin")
    public AuthenticationManager authenticationManagerDefaultAccountAdmin(@Instance("DefaultAccountAdminDetails") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        // DaoAuthenticationProvider is used because our authentication is performing by providing just USERNAME/PASSWORD
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    @Instance("DefaultAccountCandidate")
    @Primary
    public AuthenticationManager authenticationManagerDefaultAccountCandidate(@Instance("DefaultAccountCandidateDetails") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        // DaoAuthenticationProvider is used because our authentication is performing by providing just USERNAME/PASSWORD
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    @Instance("GoogleAccountCandidate")
    public AuthenticationManager authenticationManagerGoogleAccountCandidate(@Instance("GoogleAccountCandidateDetails") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
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
