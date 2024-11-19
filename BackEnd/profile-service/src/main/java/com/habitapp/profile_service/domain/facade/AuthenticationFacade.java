package com.habitapp.profile_service.domain.facade;

import com.habitapp.profile_service.dto.authentication.AccessTokenAndFingerPrintDTO;
import com.habitapp.profile_service.dto.authentication.ServiceCredentialDTO;
import com.habitapp.profile_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.profile_service.proxy.exception.common.UnexpectedException;
import com.habitapp.profile_service.proxy.exception.common.UnexpectedResponseBodyException;

public interface AuthenticationFacade {
    public boolean jwtIsRevoked(String jti) throws UnexpectedResponseBodyException, UnauthorizedException, UnexpectedException;

    public void authenticateServiceWithDefaultMethod() throws UnauthorizedException, UnexpectedException, UnexpectedResponseBodyException;
}
