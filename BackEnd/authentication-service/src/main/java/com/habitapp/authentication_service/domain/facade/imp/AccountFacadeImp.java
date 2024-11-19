package com.menara.authentication.domain.facade.imp;

import com.menara.authentication.common.constant.AccountTypeConstants;
import com.menara.authentication.common.constant.AuthenticationTypeConstants;
import com.menara.authentication.common.constant.UserTypeConstants;
import com.menara.authentication.domain.exception.account.*;
import com.menara.authentication.domain.exception.account.RoleNotDefinedException;
import com.menara.authentication.domain.exception.account.RoleNotFoundException;
import com.menara.authentication.domain.exception.account.RolePrefixException;
import com.menara.authentication.domain.exception.authentication.AuthenticationTypeNullPointerException;
import com.menara.authentication.domain.exception.authentication.UnknownAuthenticationTypeException;
import com.menara.authentication.domain.exception.general.ValueNullException;
import com.menara.authentication.domain.facade.AccountFacade;
import com.menara.authentication.annotation.Facade;
import com.menara.authentication.domain.service.AccountService;
import com.menara.authentication.domain.service.AuthenticationService;
import com.menara.authentication.dto.account.*;
import com.menara.authentication.dto.jwt.AccessTokenAndRefreshTokenAndFingerPrintDTO;
import com.menara.authentication.dto.jwt.JsonWebTokenConnectionInformationDTO;
import com.menara.authentication.dto.jwt.JwtScopeAndSubjectAndFingerprintDTO;
import com.menara.authentication.dto.jwt.JwtSubjectAndFingerprintDTO;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Facade
public class AccountFacadeImp implements AccountFacade {
    private AccountService accountService;
    private AuthenticationService authenticationService;

    @Override
    public AccountIdAndEmailAndActivationURLDTO createCandidateAccountWithDefaultMethod(AccountAndRolesAndPermissionsDTO account) throws PasswordNotFoundException,
            EmailNotFoundException,
            EmailPatternNotValidException,
            AccountAlreadyExistsException,
            RolePrefixException,
            RoleNotDefinedException,
            PermissionPrefixException,
            AccountNotCreatedException,
            PermissionNotDefinedException,
            RoleNotFoundException,
            UrlConfigurationNotFoundException,
            PasswordPatternNotValidException {
        return accountService.createCandidateAccountWithDefaultMethod(account);
    }

    @Override
    public long createAdminAccount(AccountAndRolesAndPermissionsDTO account) throws EmailNotFoundException, PasswordNotFoundException, EmailPatternNotValidException, PasswordPatternNotValidException, AccountAlreadyExistsException, RoleNotFoundException, RolePrefixException, RoleNotDefinedException, PermissionPrefixException, PermissionNotDefinedException, AccountNotCreatedException {
        List<String> roles = new ArrayList<>();
        List<String> permissions = new ArrayList<>();

        for (String role : account.getRoles()){
            if (!roles.contains(role)){
                roles.add(role);
            }
        }
        account.setRoles(roles);

        for (String permission : account.getPermissions()){
            if (!permissions.contains(permission)){
                permissions.add(permission);
            }
        }
        account.setPermissions(permissions);

        return accountService.createAdminAccountWithDefaultMethod(account);
    }

    @Override
    public void updateAdminAccountSuspensionState(long idAccount, Boolean value) throws AccountNotFoundException, ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        accountService.updateAdminAccountSuspensionState(idAccount, value);
        if(value){
           authenticationService.revokeAllAccountCurrentConnection(idAccount);
        }
    }

    @Override
    public void updateAdminAccountPassword(long idAccount, String newPassword) throws AccountNotFoundException, PasswordNotFoundException, PasswordPatternNotValidException, ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        accountService.updateAdminAccountPassword(idAccount, newPassword);
        authenticationService.revokeAllAccountCurrentConnection(idAccount);
    }

    @Override
    public void deleteOneAdminAccount(long idAccount) throws AccountNotFoundException {
        accountService.deleteOneAdminAccount(idAccount);
    }

    @Override
    public AccessTokenAndRefreshTokenAndFingerPrintDTO activateTheCandidateAccountCreatedByDefaultMethod(AccountEmailAndActivationTokenDTO accountEmailAndActivationTokenDTO,
                                                                                                         JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws EmailNotFoundException,
            EmailPatternNotValidException,
            VerificationTokenNotFoundException,
            VerificationTokenPatternNotValidException,
            VerificationTokenDurationExpiredException,
            VerificationTokensNotEqualsException,
            AccountNotFoundException,
            AccountIsActivatedException, ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        AccessTokenAndRefreshTokenAndFingerPrintDTO accessTokenAndRefreshTokenAndFingerPrint;
        AccountIdAndAuthoritiesDTO accountIdAndAuthorities;
        JwtScopeAndSubjectAndFingerprintDTO jwtScopeAndSubjectAndFingerprint;
        JwtSubjectAndFingerprintDTO jwtSubjectAndFingerprint;
        String fingerPrint;

        accountIdAndAuthorities = accountService.activateTheCandidateAccountCreatedByDefaultMethod(accountEmailAndActivationTokenDTO);
        fingerPrint = authenticationService.generateFingerprint();

        jwtScopeAndSubjectAndFingerprint =  new JwtScopeAndSubjectAndFingerprintDTO();
        jwtScopeAndSubjectAndFingerprint.setSubject(accountIdAndAuthorities.getIdAccount());
        jwtScopeAndSubjectAndFingerprint.setScope(accountIdAndAuthorities.getAuthorities());
        jwtScopeAndSubjectAndFingerprint.setFingerprint(fingerPrint);

        jwtSubjectAndFingerprint = new JwtSubjectAndFingerprintDTO();
        jwtSubjectAndFingerprint.setSubject(accountIdAndAuthorities.getIdAccount());
        jwtSubjectAndFingerprint.setFingerprint(fingerPrint);

        accessTokenAndRefreshTokenAndFingerPrint = new AccessTokenAndRefreshTokenAndFingerPrintDTO();
        accessTokenAndRefreshTokenAndFingerPrint.setRefreshToken(authenticationService
                .generateRefreshToken(jwtSubjectAndFingerprint, jsonWebTokenConnectionInformation, AuthenticationTypeConstants.ACCOUNT_JSON_WEB_TOKEN, AccountTypeConstants.CANDIDATE_DEFAULT_ACCOUNT, UserTypeConstants.USER_CANDIDATE)
                    .getRefreshToken());
        accessTokenAndRefreshTokenAndFingerPrint.setAccessToken(authenticationService
                .generateAccessToken(jwtScopeAndSubjectAndFingerprint, jsonWebTokenConnectionInformation, AuthenticationTypeConstants.ACCOUNT_JSON_WEB_TOKEN, UserTypeConstants.USER_CANDIDATE)
                    .getAccessToken());
        accessTokenAndRefreshTokenAndFingerPrint.setFingerPrint(fingerPrint);

        return accessTokenAndRefreshTokenAndFingerPrint;
    }

    @Override
    public AccountEmailAndUrlDTO generateActivationUrlForAccountCreatedByDefaultMethod(String email) throws EmailNotFoundException, EmailPatternNotValidException, VerificationTokenException, UrlConfigurationNotFoundException, AccountNotFoundException, VerificationTokenNotRegeneratedYetException {
        return accountService.generateActivationUrlForAccountCreatedByDefaultMethod(email);
    }

    @Override
    public long createCandidateAccountWithGoogleMethod(AccountEmailAndRolesAndPermissionsDTO account) throws EmailNotFoundException, EmailPatternNotValidException, AccountAlreadyExistsException, RolePrefixException, RoleNotDefinedException, PermissionPrefixException, AccountNotCreatedException, PermissionNotDefinedException, RoleNotFoundException {
        return accountService.createCandidateAccountWithGoogleMethod(account);
    }

    @Override
    public AccountEmailAndUrlDTO generateResetPasswordUrlForAccountCreatedByDefaultMethod(String email) throws UrlConfigurationNotFoundException, EmailNotFoundException, EmailPatternNotValidException, AccountNotFoundException, VerificationTokenNotRegeneratedYetException {
        return accountService.generateResetPasswordUrlForAccountCreatedByDefaultMethod(email);
    }

    @Override
    public void resetPasswordTheCandidateAccountCreatedByDefaultMethod(AccountEmailAndNewPasswordAndActivationTokenDTO accountEmailAndNewPasswordAndActivationTokenDTO) throws PasswordNotFoundException, EmailNotFoundException, EmailPatternNotValidException, VerificationTokensNotEqualsException, VerificationTokenPatternNotValidException, AccountNotFoundException, VerificationTokenDurationExpiredException, PasswordPatternNotValidException, VerificationTokenNotFoundException {
        accountService.resetPasswordTheCandidateAccountCreatedByDefaultMethod(accountEmailAndNewPasswordAndActivationTokenDTO);
    }

    @Override
    public List<String> readAllAdminRoles() {
        return accountService.readAllAdminRoles();
    }

    @Override
    public List<String> readAllAdminPermissions() {
        return accountService.readAllAdminPermissions();
    }
}
