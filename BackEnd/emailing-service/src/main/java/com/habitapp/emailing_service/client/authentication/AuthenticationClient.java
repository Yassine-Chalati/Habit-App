package com.internship_hiring_menara.emailing_service.client.authentication;

import com.internship_hiring_menara.common.http.response.authentication.AccessTokenAndFingerPrintResponseHttp;
import com.internship_hiring_menara.common.dto.service.ServiceCredentialCommonDTO;
import com.internship_hiring_menara.emailing_service.configuration.client.AuthenticationConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "authentication-service", configuration = AuthenticationConfiguration.class)
public interface AuthenticationClient {
    @PostMapping("/authentication/revoke/jwt/verify")
    public ResponseEntity<Boolean> jwtIsRevoked( String jti);

    @PostMapping("/authentication/service/default")
    public ResponseEntity<AccessTokenAndFingerPrintResponseHttp> authenticateServiceWithDefaultMethod(ServiceCredentialCommonDTO serviceCredentialCommonDTO);
}
