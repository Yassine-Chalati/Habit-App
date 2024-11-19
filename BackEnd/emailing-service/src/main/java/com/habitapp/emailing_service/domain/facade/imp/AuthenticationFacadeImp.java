package com.internship_hiring_menara.emailing_service.domain.facade.imp;

import com.internship_hiring_menara.emailing_service.annotation.Facade;
import com.internship_hiring_menara.emailing_service.configuration.record.CredentialEmailingService;
import com.internship_hiring_menara.emailing_service.domain.facade.AuthenticationFacade;
import com.internship_hiring_menara.emailing_service.dto.authentication.AccessTokenAndFingerPrintDTO;
import com.internship_hiring_menara.emailing_service.dto.authentication.ServiceCredentialDTO;
import com.internship_hiring_menara.emailing_service.proxy.client.authentication.AuthenticationServiceProxy;
import com.internship_hiring_menara.emailing_service.proxy.exception.common.UnauthorizedException;
import com.internship_hiring_menara.emailing_service.proxy.exception.common.UnexpectedException;
import com.internship_hiring_menara.emailing_service.proxy.exception.common.UnexpectedResponseBodyException;
import com.internship_hiring_menara.emailing_service.security.jwt.ServerJWTStorage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Facade
public class AuthenticationFacadeImp implements AuthenticationFacade {
    private AuthenticationServiceProxy authenticationServiceProxy;
    private ServerJWTStorage serverJWTStorage;
    private CredentialEmailingService credentialEmailingService;

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
        ServiceCredentialDTO serviceCredential = new ServiceCredentialDTO(credentialEmailingService.username(), credentialEmailingService.password());
        AccessTokenAndFingerPrintDTO accessTokenAndFingerPrint = authenticationServiceProxy.authenticateServiceWithDefaultMethod(serviceCredential);

        serverJWTStorage.setAccessToken("bearer " + accessTokenAndFingerPrint.getAccessToken());
        serverJWTStorage.setFingerprint(accessTokenAndFingerPrint.getFingerPrint());
    }
}
