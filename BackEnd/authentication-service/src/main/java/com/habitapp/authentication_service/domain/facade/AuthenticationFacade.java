package com.menara.authentication.domain.facade;

import com.menara.authentication.domain.exception.account.*;
import com.menara.authentication.domain.exception.authentication.*;
import com.menara.authentication.domain.exception.general.ValueNullException;
import com.menara.authentication.dto.account.AccountDTO;
import com.menara.authentication.dto.account.SuperAdminAccountDTO;
import com.menara.authentication.dto.authentication.*;
import com.menara.authentication.dto.connection.AccountInformationConnectionsInformationDTO;
import com.menara.authentication.dto.connection.ServiceConnectionInformationDTO;
import com.menara.authentication.dto.connection.SuperAdminConnectionInformationDTO;
import com.menara.authentication.dto.jwt.*;
import com.menara.authentication.dto.service.ServiceCredentialDTO;
import com.menara.authentication.proxy.exception.common.UnauthorizedException;
import com.menara.authentication.proxy.exception.common.UnexpectedException;
import com.menara.authentication.proxy.exception.common.UnexpectedResponseBodyException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

public interface AuthenticationFacade {
    public void authenticateAuthenticationServiceWithDefaultMethod() throws ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateServiceWithDefaultMethod(ServiceCredentialDTO credential, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateSuperAdminWithDefaultMethod(SuperAdminAccountDTO account, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws ValueNullException, InstanceOfException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateAdminWithDefaultMethod(AccountDTO account, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws AccountSuspendedException, ValueNullException, InstanceOfException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateCandidateWithDefaultMethod(AccountDTO account, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, AccountActivationException, ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateCandidateWithGoogleMethod(String googleAuthorizationCode, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, GeneralSecurityException, IOException, GoogleIdTokenNotValidException, EmailNotFoundException, EmailPatternNotValidException, AccountAlreadyExistsException, RolePrefixException, RoleNotDefinedException, PermissionPrefixException, AccountNotCreatedException, PermissionNotDefinedException, RoleNotFoundException, UnauthorizedException, UnexpectedException, ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateWithRefreshToken(String refreshToken, String fingerprint, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws AccountSuspendedException, ValueNullException, InstanceOfException, UnknownAccountTypeException, AuthenticationTypeNullPointerException, AccountActivationException, UnprocessableAccountTypeException, UnknownAuthenticationTypeException;
    public  String obtainGoogleAuthenticationUrl();
    //public void revokeJsonWebToken(JsonWebTokenJtiAndDateExpirationDTO jtiAndDateExpiration);
    public boolean jwtIsRevoked(String jti);

    public void removeExpiredJsonWebToken();

    public AuthenticationAccountDTO getAllAccountAuthentications() throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException;
    public List<AdminAuthenticationInformationDTO> getAllAdminAuthentications() throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException;
    public List<CandidateAuthenticationInformationDTO> getAllCandidateAuthentications() throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException;
    public AuthenticationInformationDTO getOneAccountAuthentications(long idAccount);
    public SuperAdminAuthenticationInformationDTO getSuperAdminAuthentication();

    public List<AccountInformationConnectionsInformationDTO> getAllAccountCurrentConnections() throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException;
    public Map<String, List<SuperAdminConnectionInformationDTO>> getAllSuperAdminCurrentConnections();
    public Map<String, List<com.menara.authentication.dto.connection.ServiceConnectionInformationDTO>> getAllServiceCurrentConnections();
    public List<AccountInformationConnectionsInformationDTO> getAllAdminCurrentConnections();
    public List<AccountInformationConnectionsInformationDTO> getAllCandidateCurrentConnections();
    public List<com.menara.authentication.dto.connection.AccountConnectionInformationDTO> getOneAccountCurrentConnections(long idAccount);
    public List<ServiceConnectionInformationDTO> getOneServiceCurrentConnections(String idService);



//    public AccessTokenDTO generateAccessToken(JwtScopeAndSubjectAndFingerprintDTO jwtScopeAndSubjectAndFingerprint, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation, String authenticationType) throws ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;
    //public RefreshTokenDTO generateRefreshToken(JwtSubjectAndFingerprintDTO jwtSubjectAndFingerprint, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation, String authenticationType) throws ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;

//    public RefreshTokenDTO generateRefreshToken(JwtSubjectAndFingerprintDTO jwtSubjectAndFingerprint, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation, String authenticationType, String accountType) throws ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;

//    public String generateFingerprint();




}
