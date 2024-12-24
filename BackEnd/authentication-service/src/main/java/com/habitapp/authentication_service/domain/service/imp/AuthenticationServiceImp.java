package com.habitapp.authentication_service.domain.service.imp;

import com.habitapp.authentication_service.annotation.Instance;
import com.habitapp.authentication_service.common.constant.JwtClaimsConstants;
import com.habitapp.authentication_service.common.utlil.generator.id.GenerateUniqueIdUtil;
import com.habitapp.authentication_service.configuration.record.JwtClaim;
import com.habitapp.authentication_service.domain.exception.authentication.AccountActivationException;
import com.habitapp.authentication_service.domain.exception.authentication.AuthenticationTypeNullPointerException;
import com.habitapp.authentication_service.domain.exception.authentication.InstanceOfException;
import com.habitapp.authentication_service.domain.exception.authentication.UnknownAuthenticationTypeException;
import com.habitapp.authentication_service.domain.exception.general.ValueNullException;
import com.habitapp.authentication_service.domain.service.AuthenticationService;
import com.habitapp.authentication_service.dto.account.AccountDTO;
import com.habitapp.authentication_service.dto.jwt.*;
import com.habitapp.authentication_service.security.userdetails.IndividualDefaultMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImp implements AuthenticationService {
    private final UserDetailsService individualDefaultAccountRefreshTokenUserDetailsService;
    private final AuthenticationManager individualDefaultMthodAuthenticationManager;
    private final AuthenticationManager authenticationManagerRefreshToken;
    private final JwtEncoder accessJwtEncoder;
    private final JwtEncoder refreshJwtEncoder;
    private final JwtClaim jwtClaim;
    private final GenerateUniqueIdUtil generateUniqueIdUtil;

    public AuthenticationServiceImp(
                                    @Instance("RefreshTokenDefaultAccountIndividualDetails") UserDetailsService individualDefaultAccountRefreshTokenUserDetailsService,
                                    @Instance("DefaultAccountIndividual") AuthenticationManager individualDefaultMthodAuthenticationManager,
                                    @Instance("RefreshToken") AuthenticationManager authenticationManagerRefreshToken,
                                    JwtClaim jwtClaim,
                                    GenerateUniqueIdUtil generateUniqueIdUtil,
                                    @Instance("accessJwtEncoder") JwtEncoder accessJwtEncoder,
                                    @Instance("refreshJwtEncoder") JwtEncoder refrshJwtEncoder,
                                    @Instance("refreshJwtDecoder") JwtDecoder refrshjwtDecoder) {
        this.individualDefaultAccountRefreshTokenUserDetailsService = individualDefaultAccountRefreshTokenUserDetailsService;
        this.individualDefaultMthodAuthenticationManager = individualDefaultMthodAuthenticationManager;
        this.authenticationManagerRefreshToken = authenticationManagerRefreshToken;
        this.accessJwtEncoder = accessJwtEncoder;
        this.refreshJwtEncoder = refrshJwtEncoder;
        this.jwtClaim = jwtClaim;
        this.generateUniqueIdUtil = generateUniqueIdUtil;
    }

    @Override
    public AccessTokenAndRefreshTokenDTO authenticateIndividualWithDefaultMethod(AccountDTO account) throws InstanceOfException, AccountActivationException, ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException {
        IndividualDefaultMethod individual;
        Object user;
        String authorities;

        Authentication authentication = individualDefaultMthodAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword())
        );

        if (!authentication.isAuthenticated()){
            throw new BadCredentialsException("username or password incorrect");
        }

        user = authentication.getPrincipal();

        if (user instanceof IndividualDefaultMethod individualDefaultMethod) {
            individual = individualDefaultMethod;
        } else
            throw new InstanceOfException("Principal is not instance of IndividualDefaultMethod");
        if (!individual.isActivated()){
            throw new AccountActivationException("account is not activated");
        }

        authorities = individual.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        return this.processGenerationToken(String.valueOf(individual.getId()), authorities);
    }

    @Override
    public AccessTokenAndRefreshTokenDTO authenticateWithRefreshToken(String refreshToken) throws ValueNullException, InstanceOfException, AccountActivationException {
        Jwt jwt;
        String authorities;

        if (refreshToken == null){
            throw new ValueNullException("refresh token is null");
        }

        BearerTokenAuthenticationToken bearerTokenAuthenticationToken = new BearerTokenAuthenticationToken(refreshToken);
        JwtAuthenticationToken jwtAuthenticationToken;
        jwtAuthenticationToken = (JwtAuthenticationToken) authenticationManagerRefreshToken.authenticate(bearerTokenAuthenticationToken);

        if (!jwtAuthenticationToken.isAuthenticated()){
            throw new InvalidBearerTokenException("refresh token is invalid");
        }

        if (jwtAuthenticationToken.getToken() instanceof Jwt token){
            jwt = token;
        } else {
            throw new InstanceOfException("Principal is not instance of Jwt");
        }

        IndividualDefaultMethod individual;
        if (individualDefaultAccountRefreshTokenUserDetailsService.loadUserByUsername(jwt.getSubject()) instanceof IndividualDefaultMethod individualDefaultMethod){
            individual = individualDefaultMethod;
        } else {
            throw new InstanceOfException("UserDetails is not instance of IndividualDefaultMethod");
        }

        if (!individual.isActivated()){
            throw new AccountActivationException("account is not activated");
        }

        authorities = individual.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        return this.processGenerationToken(String.valueOf(individual.getId()), authorities);
    }


    @Override
    public AccessTokenDTO generateAccessToken(JwtScopeAndSubjectDTO jwtScopeAndSubject) {

        String accessToken;
        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(jwtClaim.issuer())
                .expiresAt(instant.plus(jwtClaim.accessTokenExpirationTime(),ChronoUnit.HOURS))
                .issuedAt(instant)
                .audience(List.of(jwtClaim.audience()))
                .subject(jwtScopeAndSubject.getSubject())
                .id(generateUniqueIdUtil.generateUniqueId())
                .claim(JwtClaimsConstants.Client_ID, jwtClaim.clientId())
                .claim(JwtClaimsConstants.SCOPE, jwtScopeAndSubject.getScope())
                .build();

        accessToken = accessJwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        return new AccessTokenDTO(accessToken);
    }

    @Override
    public RefreshTokenDTO generateRefreshToken(JwtSubjectDTO jwtSubjectAndFingerprint) {
        String refreshToken;
        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(jwtClaim.issuer())
                .expiresAt(instant.plus(jwtClaim.refreshTokenExpirationTime(),ChronoUnit.HOURS))
                .issuedAt(instant)
                .audience(List.of(jwtClaim.audience()))
                .subject(jwtSubjectAndFingerprint.getSubject())
                .id(generateUniqueIdUtil.generateUniqueId())
                .claim(JwtClaimsConstants.Client_ID, jwtClaim.clientId())
                .build();

        refreshToken = refreshJwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        return new RefreshTokenDTO(refreshToken);
    }

    private AccessTokenAndRefreshTokenDTO processGenerationToken(String subject, String authorities) {
        AccessTokenAndRefreshTokenDTO jwtTokens = new AccessTokenAndRefreshTokenDTO(null, null);
        JwtScopeAndSubjectDTO jwtScopeAndSubject = new JwtScopeAndSubjectDTO();


        jwtScopeAndSubject.setSubject(subject);
        jwtScopeAndSubject.setScope(authorities);

        JwtSubjectDTO jwtSubjectAndFingerprint = new JwtSubjectDTO();
        jwtSubjectAndFingerprint.setSubject(subject);

        jwtTokens.setRefreshToken(this.generateRefreshToken(jwtSubjectAndFingerprint)
                .getRefreshToken());


        jwtTokens.setAccessToken(this.generateAccessToken(jwtScopeAndSubject)
                .getAccessToken());

        return jwtTokens;
    }
}
