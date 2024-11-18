package com.habitapp.profile_service.proxy.client.authentication;

import com.habitapp.common.http.response.authentication.AccessTokenAndFingerPrintResponseHttp;
import com.habitapp.common.dto.service.ServiceCredentialCommonDTO;
import com.habitapp.profile_service.annotation.Proxy;
import com.habitapp.profile_service.client.authentication.AuthenticationClient;
import com.habitapp.profile_service.dto.authentication.AccessTokenAndFingerPrintDTO;
import com.habitapp.profile_service.dto.authentication.ServiceCredentialDTO;
import com.habitapp.profile_service.proxy.exception.common.*;

import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Proxy
@AllArgsConstructor
public class AuthenticationServiceProxy {
    private AuthenticationClient authenticationClient;

    public boolean jwtIsRevoked(String jti) throws UnexpectedResponseBodyException, UnauthorizedException, UnexpectedException {
        try {
            ResponseEntity<Boolean> response = authenticationClient.jwtIsRevoked(jti);

            if (response.getStatusCode() == HttpStatus.OK
                    && response.getBody() instanceof Boolean result) {
                return result;
            } else {
                throw new UnexpectedResponseBodyException("unexpected response body from authentication service");
            }
        } catch (FeignException e){
            if (e.status() == 401) {
                throw new UnauthorizedException("unauthorized to verify the jwt revocation");
            }
            throw new UnexpectedException("unexpected error at authentication service");
        }
    }

    public AccessTokenAndFingerPrintDTO authenticateServiceWithDefaultMethod(ServiceCredentialDTO serviceCredentialDTO) throws UnauthorizedException, UnexpectedException, UnexpectedResponseBodyException {
        ServiceCredentialCommonDTO serviceCredentialCommon = new ServiceCredentialCommonDTO(serviceCredentialDTO.getUsername(), serviceCredentialDTO.getPassword());
        AccessTokenAndFingerPrintDTO accessTokenAndFingerPrint = new AccessTokenAndFingerPrintDTO();

        try {
            ResponseEntity<AccessTokenAndFingerPrintResponseHttp> response = authenticationClient.authenticateServiceWithDefaultMethod(serviceCredentialCommon);
            if (response.getStatusCode() == HttpStatus.OK
                    && response.getBody() instanceof AccessTokenAndFingerPrintResponseHttp accessTokenAndFingerPrintCommon){
                accessTokenAndFingerPrint.setAccessToken(accessTokenAndFingerPrintCommon.getAccessToken());
                accessTokenAndFingerPrint.setFingerPrint(accessTokenAndFingerPrintCommon.getFingerPrint());
                return accessTokenAndFingerPrint;
            } else
                throw new UnexpectedResponseBodyException("unexpected response body from authentication service");
        } catch (FeignException e){
            if (e.status() == 401) {
                throw new UnauthorizedException("unauthorized to authenticate to the authentication service");
            }
            throw new UnexpectedException("unexpected error at authentication service");
        }
    }

}
