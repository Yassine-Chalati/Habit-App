package com.habitapp.profile_service.domain.facade.imp;

import com.habitapp.profile_service.annotation.Facade;
import com.habitapp.profile_service.configuration.record.CredentialUserService;
import com.habitapp.profile_service.domain.facade.AuthenticationFacade;
import com.habitapp.profile_service.dto.authentication.AccessTokenAndFingerPrintDTO;
import com.habitapp.profile_service.dto.authentication.ServiceCredentialDTO;
import com.habitapp.profile_service.proxy.client.authentication.AuthenticationServiceProxy;
import com.habitapp.profile_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.profile_service.proxy.exception.common.UnexpectedException;
import com.habitapp.profile_service.proxy.exception.common.UnexpectedResponseBodyException;
import com.habitapp.profile_service.security.jwt.ServerJWTStorage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Facade
public class AuthenticationFacadeImp implements AuthenticationFacade {
    private AuthenticationServiceProxy authenticationServiceProxy;
    private ServerJWTStorage serverJWTStorage;
    private CredentialUserService credentialUserService;

    @Override
    public boolean jwtIsRevoked(String jti) throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException {
        try {
            return authenticationServiceProxy.jwtIsRevoked(jti);
        } catch (UnauthorizedException e) {
            this.authenticateServiceWithDefaultMethod();
            return authenticationServiceProxy.jwtIsRevoked(jti);
        }
    }

    @Override
    public void authenticateServiceWithDefaultMethod() throws UnauthorizedException, UnexpectedException, UnexpectedResponseBodyException {
        System.out.println("hi im in auth facade");
        System.out.println(serverJWTStorage.getAccessToken());
        System.out.println(serverJWTStorage.getFingerprint());
        ServiceCredentialDTO serviceCredential = new ServiceCredentialDTO(credentialUserService.username(), credentialUserService.password());
        AccessTokenAndFingerPrintDTO accessTokenAndFingerPrint = authenticationServiceProxy.authenticateServiceWithDefaultMethod(serviceCredential);

        serverJWTStorage.setAccessToken("bearer " + accessTokenAndFingerPrint.getAccessToken());
        serverJWTStorage.setFingerprint(accessTokenAndFingerPrint.getFingerPrint());
    }
}
