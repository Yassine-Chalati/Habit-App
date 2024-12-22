package com.habitapp.authentication_service.domain.facade;

import com.habitapp.authentication_service.dto.connection.AccountConnectionInformationDTO;
import com.habitapp.authentication_service.domain.exception.account.*;
import com.habitapp.authentication_service.domain.exception.authentication.*;
import com.habitapp.authentication_service.domain.exception.general.ValueNullException;
import com.habitapp.authentication_service.dto.account.AccountDTO;
import com.habitapp.authentication_service.dto.account.SuperAdminAccountDTO;
import com.habitapp.authentication_service.dto.authentication.*;
import com.habitapp.authentication_service.dto.connection.AccountInformationConnectionsInformationDTO;
import com.habitapp.authentication_service.dto.connection.ServiceConnectionInformationDTO;
import com.habitapp.authentication_service.dto.connection.SuperAdminConnectionInformationDTO;
import com.habitapp.authentication_service.dto.jwt.*;
import com.habitapp.authentication_service.dto.service.ServiceCredentialDTO;
import com.habitapp.authentication_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.authentication_service.proxy.exception.common.UnexpectedException;
import com.habitapp.authentication_service.proxy.exception.common.UnexpectedResponseBodyException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

public interface AuthenticationFacade {
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateIndividualWithDefaultMethod(AccountDTO account, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, AccountActivationException, ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateWithRefreshToken(String refreshToken, String fingerprint, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws AccountSuspendedException, ValueNullException, InstanceOfException, UnknownAccountTypeException, AuthenticationTypeNullPointerException, AccountActivationException, UnprocessableAccountTypeException, UnknownAuthenticationTypeException;

}
