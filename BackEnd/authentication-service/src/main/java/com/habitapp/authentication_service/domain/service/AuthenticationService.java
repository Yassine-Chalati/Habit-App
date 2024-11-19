package com.menara.authentication.domain.service;

import com.menara.authentication.domain.exception.account.AccountSuspendedException;
import com.menara.authentication.domain.exception.authentication.*;
import com.menara.authentication.domain.exception.general.ValueNullException;
import com.menara.authentication.dto.account.AccountDTO;
import com.menara.authentication.dto.account.SuperAdminAccountDTO;
import com.menara.authentication.dto.authentication.AuthenticationAccountDTO;
import com.menara.authentication.dto.authentication.AuthenticationInformationDTO;
import com.menara.authentication.dto.authentication.SuperAdminAuthenticationInformationDTO;
import com.menara.authentication.dto.connection.AccountConnectionInformationDTO;
import com.menara.authentication.dto.connection.ServiceConnectionInformationDTO;
import com.menara.authentication.dto.connection.SuperAdminConnectionInformationDTO;
import com.menara.authentication.dto.jwt.*;
import com.menara.authentication.dto.service.ServiceCredentialDTO;
import com.menara.authentication.dto.user.candidate.CandidateInformationWithoutIdAccountDTO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

public interface AuthenticationService {

    public AccessTokenAndFingerPrintDTO authenticateAuthenticationServiceWithDefaultMethod() throws ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException;
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateServiceWithDefaultMethod(ServiceCredentialDTO credential, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException;
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateSuperAdminWithDefaultMethod(SuperAdminAccountDTO account, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException;
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateAdminWithDefaultMethod(AccountDTO account, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, AccountSuspendedException, ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateCandidateWithDefaultMethod(AccountDTO account, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, AccountActivationException, ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException;
    AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateCandidateWithGoogleMethod(String email, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws UsernameNotFoundException, InstanceOfException, ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException;
    AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateWithRefreshToken(String refreshToken, String fingerprint, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws ValueNullException, InstanceOfException, UnprocessableAccountTypeException, UnknownAccountTypeException, AccountSuspendedException, AccountActivationException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;

    AccessTokenDTO generateAccessToken(JwtScopeAndSubjectAndFingerprintDTO jwtScopeAndSubjectAndFingerprint, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation, String authenticationType, String userType) throws AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException, ValueNullException;
    RefreshTokenDTO generateRefreshToken(JwtSubjectAndFingerprintDTO jwtSubjectAndFingerprint, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation, String authenticationType, String accountType, String userType) throws AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException, ValueNullException;
    public String generateFingerprint();
    public String obtainGoogleAuthenticationUrl();
    public CandidateInformationWithoutIdAccountDTO obtainCandidateSGoogleAccountInformation(String googleAuthorizationCode) throws IOException, GeneralSecurityException, GoogleIdTokenNotValidException;

    public boolean jwtIsRevoked(String jti);
    public void revokeAllAccountCurrentConnection(long idAccount) throws ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;
    public void revokeAllSuperAdminCurrentConnection(String idAccount) throws ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;
    public void revokeAllServiceCurrentConnection(String idService) throws ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException;

    public void removeExpiredJsonWebToken();

    public Map<Long, AuthenticationInformationDTO> getAllAccountAuthentications();
    public Map<Long, AuthenticationInformationDTO> getAllAdminAuthentications();
    public Map<Long, AuthenticationInformationDTO> getAllCandidateAuthentications();
    public AuthenticationInformationDTO getOneAccountAuthentications(long idAccount);
    public SuperAdminAuthenticationInformationDTO getSuperAdminAuthentication();


    public Map<Long, List<AccountConnectionInformationDTO>> getAllAccountCurrentConnections();
    public Map<String, List<SuperAdminConnectionInformationDTO>> getAllSuperAdminCurrentConnections();
    public Map<String, List<ServiceConnectionInformationDTO>> getAllServiceCurrentConnections();
    public Map<Long, List<AccountConnectionInformationDTO>> getAllAccountCurrentConnectionsByUserType(String userType);
    public List<AccountConnectionInformationDTO> getOneAccountCurrentConnections(long idAccount);
    public List<ServiceConnectionInformationDTO> getOneServiceCurrentConnections(String idService);













//    public void saveAccountJsonWebToken(JsonWebTokenJtiAndDateExpirationAndIdAccountAndConnectionInformationDTO jtiAndDateExpirationAndIdAccountAndConnectionInformation) throws ValueNullException;

//    public void saveServiceJsonWebToken(JsonWebTokenJtiAndDateExpirationAndIdServiceDTO jtiAndDateExpirationAndIdService) throws ValueNullException;

//    public void saveSuperAdminJsonWebToken(JsonWebTokenJtiAndDateExpirationAndIdAccountAndConnectionInformationDTO jtiAndDateExpirationAndIdAccountAndConnectionInformation) throws ValueNullException;

//    public void revokeJwt(JsonWebTokenJtiAndDateExpirationDTO jtiAndDateExpiration);

//    public List<AccountConnectionInformationDTO> getAllMyConnections(long idAccount) throws ConnectionNotFoundException;

//    public void revokeLotOfMyJwt(long idAccount, String[] jti);

//    public AccountConnectionsDTO getOneAccountConnections(String email);

//    public List<AccountConnectionsDTO> getAllAccountConnections();

//    public void revokeLotOfAccountJwt(String[] jtiList);

//    public List<ServiceConnectionsDTO> getAllServiceConnections();

//    public void revokeLotOfServiceJwt(String[] jtiList);

//    public List<AuthenticationConnectionDTO> getAllAccountAuthentication();

//    public List<AuthenticationConnectionDTO> getAllSuperAdminAuthentication();
}
