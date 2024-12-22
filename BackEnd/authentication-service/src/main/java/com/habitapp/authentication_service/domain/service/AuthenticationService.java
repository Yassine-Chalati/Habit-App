package com.habitapp.authentication_service.domain.service;


import com.habitapp.authentication_service.domain.exception.account.AccountSuspendedException;
import com.habitapp.authentication_service.domain.exception.authentication.*;
import com.habitapp.authentication_service.domain.exception.general.ValueNullException;
import com.habitapp.authentication_service.dto.account.AccountDTO;
import com.habitapp.authentication_service.dto.jwt.*;


public interface AuthenticationService {

    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateIndividualWithDefaultMethod(AccountDTO account) throws InstanceOfException, AccountActivationException, ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException;
    public AccessTokenDTO generateAccessToken(JwtScopeAndSubjectAndFingerprintDTO jwtScopeAndSubjectAndFingerprint);
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateWithRefreshToken(String refreshToken, String fingerprint, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws ValueNullException, InstanceOfException, UnprocessableAccountTypeException, UnknownAccountTypeException, AccountSuspendedException, AccountActivationException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;

    public RefreshTokenDTO generateRefreshToken(JwtSubjectAndFingerprintDTO jwtSubjectAndFingerprint);
    public String generateFingerprint();

}
