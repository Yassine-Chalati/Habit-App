package com.habitapp.authentication_service.domain.service;


import com.habitapp.authentication_service.domain.exception.authentication.AccountActivationException;
import com.habitapp.authentication_service.domain.exception.authentication.AuthenticationTypeNullPointerException;
import com.habitapp.authentication_service.domain.exception.authentication.InstanceOfException;
import com.habitapp.authentication_service.domain.exception.authentication.UnknownAuthenticationTypeException;
import com.habitapp.authentication_service.domain.exception.general.ValueNullException;
import com.habitapp.authentication_service.dto.account.AccountDTO;
import com.habitapp.authentication_service.dto.jwt.*;


public interface AuthenticationService {

    public AccessTokenAndRefreshTokenDTO authenticateIndividualWithDefaultMethod(AccountDTO account) throws InstanceOfException, AccountActivationException, ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException;
    public AccessTokenDTO generateAccessToken(JwtScopeAndSubjectDTO jwtScopeAndSubject);
    public AccessTokenAndRefreshTokenDTO authenticateWithRefreshToken(String refreshToken) throws ValueNullException, InstanceOfException, AccountActivationException;
    public RefreshTokenDTO generateRefreshToken(JwtSubjectDTO jwtSubjectAndFingerprint);

}
