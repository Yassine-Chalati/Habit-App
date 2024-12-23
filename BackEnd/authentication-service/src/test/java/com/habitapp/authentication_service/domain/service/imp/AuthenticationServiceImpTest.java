package com.habitapp.authentication_service.domain.service.imp;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.habitapp.authentication_service.common.utlil.generator.id.GenerateUniqueIdUtil;
import com.habitapp.authentication_service.configuration.record.JwtClaim;
import com.habitapp.authentication_service.domain.exception.authentication.AccountActivationException;
import com.habitapp.authentication_service.dto.account.AccountDTO;
import com.habitapp.authentication_service.dto.jwt.AccessTokenDTO;
import com.habitapp.authentication_service.dto.jwt.JwtScopeAndSubjectDTO;
import com.habitapp.authentication_service.dto.jwt.JwtSubjectDTO;
import com.habitapp.authentication_service.dto.jwt.RefreshTokenDTO;
import com.habitapp.authentication_service.security.userdetails.IndividualDefaultMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

class AuthenticationServiceImpTest {

    private AuthenticationServiceImp authenticationService;
    private AuthenticationManager mockAuthenticationManager;
    private UserDetailsService mockUserDetailsService;
    private JwtEncoder mockJwtEncoder;
    private JwtClaim mockJwtClaim;
    private GenerateUniqueIdUtil mockGenerateUniqueIdUtil;

    @BeforeEach
    void setUp() {
        mockAuthenticationManager = mock(AuthenticationManager.class);
        mockUserDetailsService = mock(UserDetailsService.class);
        mockJwtEncoder = mock(JwtEncoder.class);
        mockJwtClaim = mock(JwtClaim.class);
        mockGenerateUniqueIdUtil = mock(GenerateUniqueIdUtil.class);

        authenticationService = new AuthenticationServiceImp(
                mockUserDetailsService,
                mockAuthenticationManager,
                mockAuthenticationManager,
                mockJwtClaim,
                mockGenerateUniqueIdUtil,
                mockJwtEncoder,
                mockJwtEncoder,
                mock(JwtDecoder.class)
        );
    }

    @Test
    void authenticateIndividualWithDefaultMethod_throwsBadCredentialsException_whenAuthenticationFails() {
        AccountDTO account = new AccountDTO("test@example.com", "wrongPassword");
        when(mockAuthenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("username or password incorrect"));

        assertThrows(BadCredentialsException.class, () -> {
            authenticationService.authenticateIndividualWithDefaultMethod(account);
        });
    }

    @Test
    void authenticateWithRefreshToken_throwsInvalidBearerTokenException_whenTokenIsInvalid() {
        String invalidToken = "invalidToken";
        when(mockAuthenticationManager.authenticate(any())).thenThrow(new InvalidBearerTokenException("refresh token is invalid"));

        assertThrows(InvalidBearerTokenException.class, () -> {
            authenticationService.authenticateWithRefreshToken(invalidToken);
        });
    }

   
}