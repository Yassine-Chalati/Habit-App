package com.habitapp.authentication_service.domain.facade;

import com.habitapp.authentication_service.domain.exception.account.*;
import com.habitapp.authentication_service.domain.exception.authentication.*;
import com.habitapp.authentication_service.domain.exception.general.ValueNullException;
import com.habitapp.authentication_service.dto.account.AccountDTO;
import com.habitapp.authentication_service.dto.jwt.*;

public interface AuthenticationFacade {
    public AccessTokenAndRefreshTokenDTO authenticateIndividualWithDefaultMethod(AccountDTO account) throws InstanceOfException, AccountActivationException, ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException;
    public AccessTokenAndRefreshTokenDTO authenticateWithRefreshToken(String refreshToken) throws ValueNullException, InstanceOfException, AccountActivationException;
}
