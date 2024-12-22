package com.habitapp.authentication_service.domain.facade.imp;

import com.internship_hiring_menara.common.common.account.RoleNameCommonConstants;
import com.menara.authentication.annotation.Facade;
import com.menara.authentication.domain.exception.account.*;
import com.menara.authentication.domain.exception.authentication.*;
import com.habitapp.authentication_service.domain.exception.general.ValueNullException;
import com.habitapp.authentication_service.domain.facade.AuthenticationFacade;
import com.menara.authentication.domain.service.AccountService;
import com.menara.authentication.domain.service.AuthenticationService;
import com.habitapp.authentication_service.dto.account.AccountDTO;
import com.habitapp.authentication_service.dto.account.AccountEmailAndRolesAndPermissionsDTO;
import com.habitapp.authentication_service.dto.account.SuperAdminAccountDTO;
import com.menara.authentication.dto.authentication.*;
import com.habitapp.authentication_service.dto.connection.AccountConnectionInformationDTO;
import com.habitapp.authentication_service.dto.connection.AccountInformationConnectionsInformationDTO;
import com.habitapp.authentication_service.dto.connection.ServiceConnectionInformationDTO;
import com.habitapp.authentication_service.dto.connection.SuperAdminConnectionInformationDTO;
import com.menara.authentication.dto.jwt.*;
import com.habitapp.authentication_service.dto.service.ServiceCredentialDTO;
import com.habitapp.authentication_service.dto.user.admin.AdminInformationDTO;
import com.habitapp.authentication_service.dto.user.individual.CandidateInformationDTO;
import com.habitapp.authentication_service.dto.user.individual.CandidateInformationWithoutIdAccountDTO;
import com.habitapp.authentication_service.dto.user.user.CandidatesAndAdminsInformationDTO;
import com.habitapp.authentication_service.proxy.client.profile.IndividualServiceProxy;
import com.habitapp.authentication_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.authentication_service.proxy.exception.common.UnexpectedException;
import com.habitapp.authentication_service.proxy.exception.common.UnexpectedResponseBodyException;
import com.habitapp.authentication_service.security.jwt.ServerJWTStorage;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Facade
public class AuthenticationFacadeImp implements AuthenticationFacade {
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private IndividualServiceProxy individualServiceProxy;
    private AdminServiceProxy adminServiceProxy;
    private UserServiceProxy userServiceProxy;
    private ServerJWTStorage serverJWTStorage;

    @Override
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateSuperAdminWithDefaultMethod(SuperAdminAccountDTO account, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws ValueNullException, InstanceOfException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        return authenticationService.authenticateSuperAdminWithDefaultMethod(account, jsonWebTokenConnectionInformation);
    }

    @Override
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateAdminWithDefaultMethod(AccountDTO account, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws AccountSuspendedException, ValueNullException, InstanceOfException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        try {
            return authenticationService.authenticateAdminWithDefaultMethod(account, jsonWebTokenConnectionInformation);
        } catch (BadCredentialsException e){
            return authenticationService.authenticateSuperAdminWithDefaultMethod(new SuperAdminAccountDTO(account.getEmail(), account.getPassword()),
                    jsonWebTokenConnectionInformation);
        }
    }

    @Override
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateCandidateWithDefaultMethod(AccountDTO account, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, AccountActivationException, ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        return authenticationService.authenticateCandidateWithDefaultMethod(account, jsonWebTokenConnectionInformation);
    }

    @Override
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateServiceWithDefaultMethod(ServiceCredentialDTO credential, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        return authenticationService.authenticateServiceWithDefaultMethod(credential, jsonWebTokenConnectionInformation);
    }

    @Override
    public void authenticateAuthenticationServiceWithDefaultMethod() throws ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        AccessTokenAndFingerPrintDTO accessTokenAndFingerPrint = authenticationService.authenticateAuthenticationServiceWithDefaultMethod();
        serverJWTStorage.setAccessToken(accessTokenAndFingerPrint.getAccessToken());
        serverJWTStorage.setFingerprint(accessTokenAndFingerPrint.getFingerPrint());
    }

    @Override
    public String obtainGoogleAuthenticationUrl() {
        return authenticationService.obtainGoogleAuthenticationUrl();
    }

    @Override
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateCandidateWithGoogleMethod(String googleAuthorizationCode, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, GeneralSecurityException, IOException, GoogleIdTokenNotValidException, EmailNotFoundException, EmailPatternNotValidException, AccountAlreadyExistsException, RolePrefixException, RoleNotDefinedException, PermissionPrefixException, AccountNotCreatedException, PermissionNotDefinedException, RoleNotFoundException, UnauthorizedException, UnexpectedException, ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        CandidateInformationWithoutIdAccountDTO candidateInformationWithoutIdAccount = authenticationService.obtainCandidateSGoogleAccountInformation(googleAuthorizationCode);
        CandidateInformationDTO candidateInformation;
        try {
            return authenticationService.authenticateCandidateWithGoogleMethod(candidateInformationWithoutIdAccount.getEmail(), jsonWebTokenConnectionInformation);
        } catch (BadCredentialsException e){
            long idAccount = 0;
            AccountEmailAndRolesAndPermissionsDTO account = new AccountEmailAndRolesAndPermissionsDTO();
            account.setEmail(candidateInformationWithoutIdAccount.getEmail());
            account.setRoles(List.of(RoleNameCommonConstants.NEW_CANDIDATE));

            idAccount = accountService.createCandidateAccountWithGoogleMethod(account);

            candidateInformation = new CandidateInformationDTO();
            candidateInformation.setIdAccount(idAccount);
            candidateInformation.setFirstName(candidateInformationWithoutIdAccount.getFirstName());
            candidateInformation.setLastName(candidateInformationWithoutIdAccount.getLastName());
            candidateInformation.setEmail(candidateInformationWithoutIdAccount.getEmail());
            candidateInformation.setGender(candidateInformationWithoutIdAccount.getGender());
            candidateInformation.setImageUrl(candidateInformationWithoutIdAccount.getImageUrl());

            try {
                individualServiceProxy.createCandidate(candidateInformation);
            } catch (UnexpectedException ex) {
                accountService.deleteCandidateGoogleAccountById(idAccount);
                throw ex;
            } catch (UnauthorizedException ex){
                System.out.println("generate jwt server");
                this.authenticateAuthenticationServiceWithDefaultMethod();
                try {
                    individualServiceProxy.createCandidate(candidateInformation);
                } catch (UnexpectedException | UnauthorizedException exc) {
                    accountService.deleteCandidateGoogleAccountById(idAccount);
                    throw exc;
                }
            }
            return authenticationService.authenticateCandidateWithGoogleMethod(candidateInformationWithoutIdAccount.getEmail(), jsonWebTokenConnectionInformation);
        }
    }

    @Override
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateWithRefreshToken(String refreshToken, String fingerprint, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws AccountSuspendedException, ValueNullException, InstanceOfException, UnknownAccountTypeException, AuthenticationTypeNullPointerException, AccountActivationException, UnprocessableAccountTypeException, UnknownAuthenticationTypeException {
        return authenticationService.authenticateWithRefreshToken(refreshToken, fingerprint, jsonWebTokenConnectionInformation);
    }

    /*@Override
    public void revokeJsonWebToken(JsonWebTokenJtiAndDateExpirationDTO jtiAndDateExpiration) {
        authenticationService.revokeMyJwt(jtiAndDateExpiration);
    }*/

    @Override
    public boolean jwtIsRevoked(String jti) {
        return authenticationService.jwtIsRevoked(jti);
    }

    @Override
    public void removeExpiredJsonWebToken() {
        authenticationService.removeExpiredJsonWebToken();
    }

    @Override
    public AuthenticationAccountDTO getAllAccountAuthentications() throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException {
        AuthenticationAccountDTO authenticationAccountDTO = new AuthenticationAccountDTO(new ArrayList<>(), new ArrayList<>());
        AuthenticationInformationDTO authenticationInformationDTO;

        Map<Long, AuthenticationInformationDTO> authenticationInformationMap = authenticationService.getAllAccountAuthentications();
        CandidatesAndAdminsInformationDTO candidatesAndAdminsInformationDTO = userServiceProxy.readAllCandidatesAndAdmins();

        for (CandidateInformationDTO candidate : candidatesAndAdminsInformationDTO.getCandidateInformationList()){
            authenticationInformationDTO = authenticationInformationMap.get(candidate.getIdAccount());
            if (authenticationInformationDTO == null){
                continue;
//                authenticationInformationDTO = new AuthenticationInformationDTO(0, "notYet", null, "notYet", "notYet", UserTypeConstants.USER_CANDIDATE);
            }

            authenticationAccountDTO.getCandidateAuthenticationInformationList().add(
                    new CandidateAuthenticationInformationDTO(candidate.getIdAccount(),
                            candidate.getFirstName(),
                            candidate.getLastName(),
                            candidate.getEmail(),
                            candidate.getImageUrl(),
                            candidate.getGender(),
                    authenticationInformationDTO.getLastIpConnection(),
                    authenticationInformationDTO.getLastConnection(),
                    authenticationInformationDTO.getUserAgent(),
                    authenticationInformationDTO.getScreenResolution(),
                    authenticationInformationDTO.getUserType()));
        }
        for (AdminInformationDTO admin : candidatesAndAdminsInformationDTO.getAdminInformationList()){
            authenticationInformationDTO = authenticationInformationMap.get(admin.getIdAccount());
            if (authenticationInformationDTO == null){
//                authenticationInformationDTO = new AuthenticationInformationDTO(0, "notYet", null, "notYet", "notYet", UserTypeConstants.USER_ADMIN);
                continue;
            }

            authenticationAccountDTO.getAdminAuthenticationInformationList().add(
                    new AdminAuthenticationInformationDTO(admin.getIdAccount(),
                            admin.getFirstName(),
                            admin.getLastName(),
                            admin.getEmail(),
                            admin.getRegistration(),
                            authenticationInformationDTO.getLastIpConnection(),
                            authenticationInformationDTO.getLastConnection(),
                            authenticationInformationDTO.getUserAgent(),
                            authenticationInformationDTO.getScreenResolution(),
                            authenticationInformationDTO.getUserType()));
        }

        return authenticationAccountDTO;
    }

    @Override
    public List<AdminAuthenticationInformationDTO> getAllAdminAuthentications() throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException {
        List<AdminAuthenticationInformationDTO> adminAuthenticationInformationList = new ArrayList<>();
        AuthenticationInformationDTO authenticationInformationDTO;

        Map<Long, AuthenticationInformationDTO> authenticationInformationMap = authenticationService.getAllAdminAuthentications();
        List<AdminInformationDTO> adminInformationList = adminServiceProxy.readAllAdmins().values().stream().toList();

        for (AdminInformationDTO admin : adminInformationList){
            authenticationInformationDTO = authenticationInformationMap.get(admin.getIdAccount());
            if (authenticationInformationDTO == null){
                continue;
            }

            adminAuthenticationInformationList.add(new AdminAuthenticationInformationDTO(admin.getIdAccount(),
                    admin.getFirstName(),
                    admin.getLastName(),
                    admin.getEmail(),
                    admin.getRegistration(),
                    authenticationInformationDTO.getLastIpConnection(),
                    authenticationInformationDTO.getLastConnection(),
                    authenticationInformationDTO.getUserAgent(),
                    authenticationInformationDTO.getScreenResolution(),
                    authenticationInformationDTO.getUserType()));
        }

        return adminAuthenticationInformationList;
    }

    @Override
    public List<CandidateAuthenticationInformationDTO> getAllCandidateAuthentications() throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException {
        List<CandidateAuthenticationInformationDTO> candidateAuthenticationInformationList = new ArrayList<>();
        AuthenticationInformationDTO authenticationInformationDTO;

        Map<Long, AuthenticationInformationDTO> authenticationInformationMap = authenticationService.getAllCandidateAuthentications();
        List<CandidateInformationDTO> candidateInformationList = individualServiceProxy.readAllCandidates().values().stream().toList();

        for (CandidateInformationDTO candidate : candidateInformationList){
            authenticationInformationDTO = authenticationInformationMap.get(candidate.getIdAccount());
            if (authenticationInformationDTO == null){
                continue;
            }

            candidateAuthenticationInformationList.add(new CandidateAuthenticationInformationDTO(candidate.getIdAccount(),
                    candidate.getFirstName(),
                    candidate.getLastName(),
                    candidate.getEmail(),
                    candidate.getImageUrl(),
                    candidate.getGender(),
                    authenticationInformationDTO.getLastIpConnection(),
                    authenticationInformationDTO.getLastConnection(),
                    authenticationInformationDTO.getUserAgent(),
                    authenticationInformationDTO.getScreenResolution(),
                    authenticationInformationDTO.getUserType()));
        }

        return candidateAuthenticationInformationList;
    }

    @Override
    public AuthenticationInformationDTO getOneAccountAuthentications(long idAccount) {
        return authenticationService.getOneAccountAuthentications(idAccount);
    }

    @Override
    public SuperAdminAuthenticationInformationDTO getSuperAdminAuthentication() {
        return authenticationService.getSuperAdminAuthentication();
    }

    @Override
    public List<AccountInformationConnectionsInformationDTO> getAllAccountCurrentConnections() throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException {
        List<AccountInformationConnectionsInformationDTO> accountInformationConnectionsInformationDTOList = new ArrayList<>();

        Map<Long, List<AccountConnectionInformationDTO>> listMap = authenticationService.getAllAccountCurrentConnections();
        CandidatesAndAdminsInformationDTO candidatesAndAdminsInformationDTO = userServiceProxy.readAllCandidatesAndAdmins();


        return null;
    }

    @Override
    public Map<String, List<SuperAdminConnectionInformationDTO>> getAllSuperAdminCurrentConnections() {
        return null;
    }

    @Override
    public Map<String, List<ServiceConnectionInformationDTO>> getAllServiceCurrentConnections() {
        return null;
    }

    @Override
    public List<AccountInformationConnectionsInformationDTO> getAllAdminCurrentConnections() {
        return null;
    }

    @Override
    public List<AccountInformationConnectionsInformationDTO> getAllCandidateCurrentConnections() {
        return null;
    }

    @Override
    public List<AccountConnectionInformationDTO> getOneAccountCurrentConnections(long idAccount) {
        return null;
    }

    @Override
    public List<ServiceConnectionInformationDTO> getOneServiceCurrentConnections(String idService) {
        return null;
    }
}
