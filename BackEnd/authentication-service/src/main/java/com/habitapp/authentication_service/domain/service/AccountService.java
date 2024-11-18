package com.menara.authentication.domain.service;

import com.menara.authentication.domain.exception.account.*;
import com.menara.authentication.domain.exception.general.ValueNullException;
import com.menara.authentication.dto.account.*;
import com.menara.authentication.dto.account.AccountIdAndEmailAndActivationURLDTO;

import java.util.List;
import java.util.Map;

public interface AccountService {
    public long createAdminAccountWithDefaultMethod(AccountAndRolesAndPermissionsDTO account) throws EmailNotFoundException, PasswordNotFoundException, EmailPatternNotValidException, PasswordPatternNotValidException, AccountAlreadyExistsException, RoleNotFoundException, RolePrefixException, RoleNotDefinedException, PermissionPrefixException, PermissionNotDefinedException, AccountNotCreatedException;
//    public void reCreateAdminAccountWithDefaultMethod(AdminAccountWithPasswordDTO account);
    public AccountIdAndEmailAndActivationURLDTO createCandidateAccountWithDefaultMethod(AccountAndRolesAndPermissionsDTO account) throws EmailPatternNotValidException, PasswordPatternNotValidException, EmailNotFoundException, PasswordNotFoundException, UrlConfigurationNotFoundException, AccountAlreadyExistsException, RoleNotFoundException, RolePrefixException, RoleNotDefinedException, AccountNotCreatedException, PermissionPrefixException, PermissionNotDefinedException;
    public AccountIdAndAuthoritiesDTO activateTheCandidateAccountCreatedByDefaultMethod(AccountEmailAndActivationTokenDTO accountEmailAndActivationTokenDTO) throws EmailNotFoundException, VerificationTokenNotFoundException, EmailPatternNotValidException, VerificationTokenPatternNotValidException, AccountNotFoundException, VerificationTokenDurationExpiredException, AccountIsActivatedException, VerificationTokensNotEqualsException;
    public AccountEmailAndUrlDTO generateActivationUrlForAccountCreatedByDefaultMethod(String email) throws UrlConfigurationNotFoundException, EmailNotFoundException, EmailPatternNotValidException, AccountNotFoundException, VerificationTokenException, VerificationTokenNotRegeneratedYetException;
    public long createCandidateAccountWithGoogleMethod(AccountEmailAndRolesAndPermissionsDTO account) throws EmailNotFoundException, EmailPatternNotValidException, AccountAlreadyExistsException, RoleNotFoundException, RolePrefixException, RoleNotDefinedException, PermissionPrefixException, PermissionNotDefinedException, AccountNotCreatedException;
    public void deleteCandidateGoogleAccountById(long id);
    public AccountEmailAndUrlDTO generateResetPasswordUrlForAccountCreatedByDefaultMethod(String email) throws UrlConfigurationNotFoundException, EmailNotFoundException, EmailPatternNotValidException, AccountNotFoundException, VerificationTokenNotRegeneratedYetException;
    public void resetPasswordTheCandidateAccountCreatedByDefaultMethod(AccountEmailAndNewPasswordAndActivationTokenDTO accountEmailAndNewPasswordAndActivationTokenDTO) throws EmailNotFoundException, VerificationTokenNotFoundException, EmailPatternNotValidException, VerificationTokenPatternNotValidException, AccountNotFoundException, VerificationTokenDurationExpiredException, PasswordNotFoundException, PasswordPatternNotValidException, VerificationTokensNotEqualsException;
    public List<String> readAllAdminRoles();
    public List<String> readAllAdminPermissions();
    public List<AdminAccountDTO> readAllAdminsAccount();
    public List<AdminAccountDTO> readAdminsAccountByKeywords(List<Long> ids);
    public AdminAccountDTO readAdminAccountById(Long id) throws AccountNotFoundException;
    public AdminAccountWithPasswordDTO readAdminAccountAndPasswordById(Long id) throws AccountNotFoundException;
    public Map<Long,AdminAccountDTO> readAllSuspendedAdminsAccount();
    public void updateAdminAccountSuspensionState(long idAccount, Boolean value) throws AccountNotFoundException, ValueNullException;
    public void updateAdminAccountPassword(long idAccount, String newPassword) throws AccountNotFoundException, PasswordNotFoundException, PasswordPatternNotValidException;
    public AdminAccountDTO updateAdminAccountEmailOrRolesOrPermissions(AccountIdAndEmailAndRolesAndPermissionsDTO adminAccountNewModifications) throws AccountNotFoundException, EmailNotFoundException, EmailPatternNotValidException, AccountAlreadyExistsException, RolePrefixException, RoleNotDefinedException, PermissionPrefixException, PermissionNotDefinedException, ValueNullException;
    public void deleteOneAdminAccount(long idAccount) throws AccountNotFoundException;
    /*public void deleteGroupAdminAccount(List<Long> ids);
    public void deleteAllAdminAccount();*/

}
