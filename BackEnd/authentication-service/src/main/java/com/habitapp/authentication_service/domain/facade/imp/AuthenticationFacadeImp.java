package com.habitapp.authentication_service.domain.facade.imp;

import com.habitapp.authentication_service.annotation.Facade;
import com.habitapp.authentication_service.domain.exception.authentication.AccountActivationException;
import com.habitapp.authentication_service.domain.exception.authentication.AuthenticationTypeNullPointerException;
import com.habitapp.authentication_service.domain.exception.authentication.InstanceOfException;
import com.habitapp.authentication_service.domain.exception.authentication.UnknownAuthenticationTypeException;
import com.habitapp.authentication_service.domain.exception.general.ValueNullException;
import com.habitapp.authentication_service.domain.facade.AuthenticationFacade;
import com.habitapp.authentication_service.domain.service.AuthenticationService;
import com.habitapp.authentication_service.dto.account.AccountDTO;
import com.habitapp.authentication_service.dto.jwt.AccessTokenAndRefreshTokenDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Facade
public class AuthenticationFacadeImp implements AuthenticationFacade {
    private AuthenticationService authenticationService;

    @Override
    public AccessTokenAndRefreshTokenDTO authenticateIndividualWithDefaultMethod(AccountDTO account) throws InstanceOfException, AccountActivationException, ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        return authenticationService.authenticateIndividualWithDefaultMethod(account);
    }

    @Override
    public AccessTokenAndRefreshTokenDTO authenticateWithRefreshToken(String refreshToken) throws ValueNullException, InstanceOfException, AccountActivationException{
        return authenticationService.authenticateWithRefreshToken(refreshToken);
    }
}
