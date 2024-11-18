package com.habitapp.profile_service.client.authentication;

import com.habitapp.profile_service.configuration.client.AuthenticationConfiguration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.habitapp.common.dto.service.ServiceCredentialCommonDTO;
import com.habitapp.common.http.response.authentication.AccessTokenAndFingerPrintResponseHttp;


@FeignClient(name = "authentication-service", configuration = AuthenticationConfiguration.class)
public interface AuthenticationClient {
    @PostMapping("/authentication/revoke/jwt/verify")
    public ResponseEntity<Boolean> jwtIsRevoked(String jti);

    @PostMapping("/authentication/service/default")
    public ResponseEntity<AccessTokenAndFingerPrintResponseHttp> authenticateServiceWithDefaultMethod(@RequestBody ServiceCredentialCommonDTO serviceCredentialCommonDTO);
}
