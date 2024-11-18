package com.internship_hiring_menara.emailing_service.domain.facade;


import com.internship_hiring_menara.emailing_service.proxy.exception.common.UnauthorizedException;
import com.internship_hiring_menara.emailing_service.proxy.exception.common.UnexpectedException;
import com.internship_hiring_menara.emailing_service.proxy.exception.common.UnexpectedResponseBodyException;

public interface AuthenticationFacade {
    public boolean jwtIsRevoked(String jti) throws UnexpectedResponseBodyException, UnauthorizedException, UnexpectedException;

    public void authenticateServiceWithDefaultMethod() throws UnauthorizedException, UnexpectedException, UnexpectedResponseBodyException;
}
