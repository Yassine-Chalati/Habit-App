package com.menara.authentication.controller;

import com.internship_hiring_menara.common.common.account.RoleNameCommonConstants;
import com.internship_hiring_menara.common.http.request.admin.*;
import com.internship_hiring_menara.common.http.request.search.SearchWithKeywordsRequestHttp;
import com.internship_hiring_menara.common.http.request_response.admin.AdminRequestResponseHttp;
import com.internship_hiring_menara.common.http.request.account.AccountRequestHttp;
import com.internship_hiring_menara.common.http.response.admin.AdminAccountInformationResponseHttp;
import com.internship_hiring_menara.common.http.response.admin.AdminAccountInformationResponseHttpList;
import com.menara.authentication.common.constant.AccountHttpResponseConstants;
import com.menara.authentication.common.constant.CookiesNameConstants;
import com.menara.authentication.common.handler.time.TimeHandler;
import com.menara.authentication.configuration.record.CookiesAttributesValues;
import com.menara.authentication.configuration.record.HttpResponseTime;
import com.menara.authentication.configuration.record.JwtClaim;
import com.menara.authentication.domain.exception.account.*;
import com.menara.authentication.domain.exception.account.RoleNotDefinedException;
import com.menara.authentication.domain.exception.account.RoleNotFoundException;
import com.menara.authentication.domain.exception.account.RolePrefixException;
import com.menara.authentication.domain.exception.admin.AdminsIdsNotFoundException;
import com.menara.authentication.domain.exception.admin.KeywordsNotFoundException;
import com.menara.authentication.domain.exception.admin.MissingAdminInformationException;
import com.menara.authentication.domain.exception.authentication.AuthenticationTypeNullPointerException;
import com.menara.authentication.domain.exception.authentication.UnknownAuthenticationTypeException;
import com.menara.authentication.domain.exception.general.ValueNullException;
import com.menara.authentication.domain.facade.*;
import com.menara.authentication.dto.account.*;
import com.menara.authentication.dto.jwt.JsonWebTokenConnectionInformationDTO;
import com.menara.authentication.dto.user.admin.AdminInformationDTO;
import com.menara.authentication.dto.user.candidate.CandidateInformationDTO;
import com.menara.authentication.dto.email.EmailAndUrlDTO;
import com.menara.authentication.dto.jwt.AccessTokenAndRefreshTokenAndFingerPrintDTO;
import com.menara.authentication.dto.jwt.AccessTokenAndRefreshTokenDTO;
import com.menara.authentication.proxy.exception.common.*;
import com.menara.authentication.proxy.exception.user.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {
    private AccountFacade accountFacade;
    private EmailFacade emailFacade;
    private AuthenticationFacade authenticationFacade;
    private JwtClaim jwtClaim;
    private CookiesAttributesValues cookiesAttributesValues;
    private CandidateFacade candidateFacade;
    private AdminFacade adminFacade;
    private HttpResponseTime responseTime;

    @PostMapping("/admin/default-method/create")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<?> createAdminAccountWithDefaultMethod(@RequestBody AdminAccountAndRolesAndPermissionsDTO account) {
        long idAccount = 0;
        AdminInformationDTO adminInformation;
        HttpHeaders httpHeaders = new HttpHeaders();
        AccountAndRolesAndPermissionsDTO accountAndRolesAndPermissionsDTO;
        AdminRequestResponseHttp adminResponseHttp = new AdminRequestResponseHttp();
        AdminInformationDTO adminInformationDTO;

        accountAndRolesAndPermissionsDTO = new AccountAndRolesAndPermissionsDTO(account.getEmail(), account.getPassword(), account.getRoles(), account.getPermissions());

        try {
            idAccount = accountFacade.createAdminAccount(accountAndRolesAndPermissionsDTO);
        } catch (PasswordNotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(AccountHttpResponseConstants.PASSWORD_NOT_FOUND, httpHeaders, HttpStatus.BAD_REQUEST);
        } catch (EmailNotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(AccountHttpResponseConstants.EMAIL_NOT_FOUND, httpHeaders, HttpStatus.BAD_REQUEST);
        } catch (EmailPatternNotValidException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(AccountHttpResponseConstants.EMAIL_PATTERN_NOT_VALID, httpHeaders, HttpStatus.BAD_REQUEST);
        } catch (AccountAlreadyExistsException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(AccountHttpResponseConstants.ACCOUNT_CREATION, httpHeaders, HttpStatus.OK);
        } catch (RolePrefixException | RoleNotDefinedException | PermissionPrefixException |
                 PermissionNotDefinedException | RoleNotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(AccountHttpResponseConstants.ERROR_AT_CREATION_ACCOUNT, httpHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (AccountNotCreatedException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(AccountHttpResponseConstants.ACCOUNT_NOT_CREATED, httpHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (PasswordPatternNotValidException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(AccountHttpResponseConstants.PASSWORD_PATTERN_NOT_VALID, httpHeaders, HttpStatus.BAD_REQUEST);
        }

        adminInformation = new AdminInformationDTO();

        adminInformation.setIdAccount(idAccount);
        adminInformation.setFirstName(account.getFirstName());
        adminInformation.setLastName(account.getLastName());
        adminInformation.setEmail(account.getEmail());
        adminInformation.setRegistration(account.getRegistration());

        try {
            System.out.println("i m in adminFacade.createAdmin(adminInformation);");
            adminInformationDTO = adminFacade.createAdmin(adminInformation);
            adminResponseHttp.setIdAccount(adminInformationDTO.getIdAccount());
            adminResponseHttp.setFirstName(adminInformationDTO.getFirstName());
            adminResponseHttp.setLastName(adminInformationDTO.getLastName());
            adminResponseHttp.setEmail(adminInformationDTO.getEmail());
            adminResponseHttp.setRegistration(adminInformationDTO.getRegistration());
            return new ResponseEntity<>(adminResponseHttp, httpHeaders, HttpStatus.OK);
        } catch (UnexpectedException e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            try {
                authenticationFacade.authenticateAuthenticationServiceWithDefaultMethod();
                adminInformationDTO = adminFacade.createAdmin(adminInformation);
                adminResponseHttp.setIdAccount(adminInformationDTO.getIdAccount());
                adminResponseHttp.setFirstName(adminInformationDTO.getFirstName());
                adminResponseHttp.setLastName(adminInformationDTO.getLastName());
                adminResponseHttp.setEmail(adminInformationDTO.getEmail());
                adminResponseHttp.setRegistration(adminInformationDTO.getRegistration());
                return new ResponseEntity<>(adminResponseHttp, httpHeaders, HttpStatus.OK);
            } catch (ValueNullException | AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException |
                     UnexpectedException | UnauthorizedException | AdminNotCreatedException ex) {
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (UserProfileInformationNotProvidedException | IdAccountNotFoundException ex) {
                System.out.println(ex.getMessage());
                try {
                    accountFacade.deleteOneAdminAccount(idAccount);
                } catch (AccountNotFoundException exc) {
                    System.out.println(exc.getMessage());
                    return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }
        } catch (AdminNotCreatedException e) {
            System.out.println("AdminNotCreatedException");
            System.out.println(e.getMessage());
            try {
                accountFacade.deleteOneAdminAccount(idAccount);
            } catch (AccountNotFoundException ex) {
                System.out.println(ex.getMessage());
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserProfileInformationNotProvidedException | IdAccountNotFoundException e) {
            System.out.println(e.getMessage());
            try {
                accountFacade.deleteOneAdminAccount(idAccount);
            } catch (AccountNotFoundException ex) {
                System.out.println(ex.getMessage());
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/candidate/default-method/create")
    public ResponseEntity<String> createCandidateAccountWithDefaultMethod(@RequestBody AccountRequestHttp accountRequestHttp){
        TimeHandler timeHandler = new TimeHandler(responseTime.responseTimeWithDispatching());
        timeHandler.start();
        AccountIdAndEmailAndActivationURLDTO accountCreatedInformation;
        EmailAndUrlDTO emailAndURL;
        HttpHeaders httpHeaders = new HttpHeaders();
        AccountAndRolesAndPermissionsDTO account = new AccountAndRolesAndPermissionsDTO(accountRequestHttp.getEmail(), accountRequestHttp.getPassword(), List.of(RoleNameCommonConstants.NEW_CANDIDATE), List.of());

        try {
            accountCreatedInformation = accountFacade.createCandidateAccountWithDefaultMethod(account);
            emailAndURL = new EmailAndUrlDTO(accountCreatedInformation.getEmail(), accountCreatedInformation.getActivationURL());
        } catch (PasswordNotFoundException e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.PASSWORD_NOT_FOUND, httpHeaders, HttpStatus.BAD_REQUEST);
        } catch (EmailNotFoundException e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.EMAIL_NOT_FOUND, httpHeaders, HttpStatus.BAD_REQUEST);
        } catch (EmailPatternNotValidException e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.EMAIL_PATTERN_NOT_VALID, httpHeaders, HttpStatus.BAD_REQUEST);
        } catch (AccountAlreadyExistsException e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.ACCOUNT_CREATION, httpHeaders, HttpStatus.OK);
        } catch (RolePrefixException | RoleNotDefinedException | PermissionPrefixException |
                 PermissionNotDefinedException | RoleNotFoundException e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.ERROR_AT_CREATION_ACCOUNT, httpHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (AccountNotCreatedException e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.ACCOUNT_NOT_CREATED, httpHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (UrlConfigurationNotFoundException e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.ERROR_AT_CREATION_ACCOUNT, httpHeaders, HttpStatus.BAD_REQUEST);
        } catch (PasswordPatternNotValidException e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.PASSWORD_PATTERN_NOT_VALID, httpHeaders, HttpStatus.BAD_REQUEST);
        }

        try {
            candidateFacade.createCandidate(new CandidateInformationDTO(accountCreatedInformation.getIdAccount(), accountCreatedInformation.getEmail()));
            emailFacade.sendURLActivationAccount(emailAndURL);
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.ACCOUNT_CREATION, httpHeaders, HttpStatus.OK);
        } catch (UnprocessableEntityException e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.UNPROCESSABLE_ENTITY, httpHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (ForbiddenException | InternalServerErrorException e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.ERROR_AT_CREATION_ACCOUNT, httpHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (UnauthorizedException e) {

            try {
                authenticationFacade.authenticateAuthenticationServiceWithDefaultMethod();
                candidateFacade.createCandidate(new CandidateInformationDTO(accountCreatedInformation.getIdAccount(), accountCreatedInformation.getEmail()));
                emailFacade.sendURLActivationAccount(emailAndURL);
                timeHandler.timingEqualization();
                return new ResponseEntity<>(AccountHttpResponseConstants.ACCOUNT_CREATION, httpHeaders, HttpStatus.OK);
            } catch (UnprocessableEntityException ex) {
                System.out.println(e.getMessage());
                timeHandler.timingEqualization();
                return new ResponseEntity<>(AccountHttpResponseConstants.UNPROCESSABLE_ENTITY, httpHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
            } catch (ForbiddenException | InternalServerErrorException | ValueNullException |
                     AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException ex) {
                System.out.println(e.getMessage());
                timeHandler.timingEqualization();
                return new ResponseEntity<>(AccountHttpResponseConstants.ERROR_AT_CREATION_ACCOUNT, httpHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
            } catch (UnauthorizedException ex) {
                System.out.println(e.getMessage());
                timeHandler.timingEqualization();
                return new ResponseEntity<>(AccountHttpResponseConstants.ERROR_AT_CREATION_ACCOUNT, httpHeaders, HttpStatus.UNAUTHORIZED );
            } catch (UnexpectedException ex) {
                timeHandler.timingEqualization();
                return new ResponseEntity<>(AccountHttpResponseConstants.ERROR_AT_CREATION_ACCOUNT, httpHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
            }

        } catch (UnexpectedException e) {
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.ERROR_AT_CREATION_ACCOUNT, httpHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/candidate/default-method/activate/token/generate")
    public ResponseEntity<?> obtainActivationUrlForAccountCreatedByDefaultMethod(@RequestBody String email){
        TimeHandler timeHandler = new TimeHandler(responseTime.responseTimeWithDispatching());
        timeHandler.start();

        AccountEmailAndUrlDTO accountEmailAndActivationURL;

        try {
            accountEmailAndActivationURL = accountFacade.generateActivationUrlForAccountCreatedByDefaultMethod(email);
        } catch (EmailNotFoundException | EmailPatternNotValidException | AccountNotFoundException
                 | VerificationTokenException ex) {
            timeHandler.timingEqualization();
            return new ResponseEntity<>("the new activation url account is sent to your email",
                    new HttpHeaders(),
                    HttpStatus.OK);
//            return new ResponseEntity<String>("email not valid", new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch ( UrlConfigurationNotFoundException ex) {
            timeHandler.timingEqualization();
            return new ResponseEntity<>( new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (VerificationTokenNotRegeneratedYetException ex) {
            /*return new ResponseEntity<String>("the activation url account can be send after "
                    + durationResendUrl.activationUrl()
                    + " hours from last sent activation url to your email",
                    new HttpHeaders(),
                    HttpStatus.UNAUTHORIZED);*/
            timeHandler.timingEqualization();
            return new ResponseEntity<>("the new activation url account is sent to your email",
                    new HttpHeaders(),
                    HttpStatus.OK);
        }

        try {
            emailFacade.sendURLActivationAccount(new EmailAndUrlDTO(accountEmailAndActivationURL.getEmail(), accountEmailAndActivationURL.getUrl()));
            timeHandler.timingEqualization();
            return new ResponseEntity<>("the new activation url account is sent to your email",
                    new HttpHeaders(),
                    HttpStatus.OK);
        } catch (UnprocessableEntityException | ForbiddenException | InternalServerErrorException |
                 UnexpectedException ex) {
            timeHandler.timingEqualization();
            return new ResponseEntity<>( new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException ex) {
            try {
                authenticationFacade.authenticateAuthenticationServiceWithDefaultMethod();
                emailFacade.sendURLActivationAccount(new EmailAndUrlDTO(accountEmailAndActivationURL.getEmail(), accountEmailAndActivationURL.getUrl()));
                timeHandler.timingEqualization();
                return new ResponseEntity<>("the new activation url account is sent to your email",
                        new HttpHeaders(),
                        HttpStatus.OK);
            } catch (UnprocessableEntityException | ForbiddenException | InternalServerErrorException |
                     UnauthorizedException | UnexpectedException | ValueNullException |
                     AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException exc) {
                timeHandler.timingEqualization();
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/candidate/default-method/activate/token/{token}")
    public ResponseEntity<?> activateCandidateAccountWithDefaultMethod(@PathVariable String token, @RequestParam("email") String email, String screenResolution, HttpServletRequest request){
        TimeHandler timeHandler = new TimeHandler(responseTime.responseTimeWithoutDispatching());
        timeHandler.start();

        JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation = new JsonWebTokenConnectionInformationDTO();

        jsonWebTokenConnectionInformation.setIp(request.getRemoteAddr());
        jsonWebTokenConnectionInformation.setScreenResolution(screenResolution);
        jsonWebTokenConnectionInformation.setUserAgent(request.getHeader("User-Agent"));

        AccessTokenAndRefreshTokenAndFingerPrintDTO accessTokenAndRefreshTokenAndFingerPrint;
        AccessTokenAndRefreshTokenDTO accessTokenAndRefreshToken;
        HttpHeaders headers = new HttpHeaders();

        try {
            accessTokenAndRefreshTokenAndFingerPrint = accountFacade.activateTheCandidateAccountCreatedByDefaultMethod(new AccountEmailAndActivationTokenDTO(email, token), jsonWebTokenConnectionInformation);
            headers.add(HttpHeaders.SET_COOKIE, cookiesAttributesValues.prefixHost()
                    + CookiesNameConstants.TOKEN_FINGERPRINT
                    + "="
                    + accessTokenAndRefreshTokenAndFingerPrint.getFingerPrint()
                    + "; HttpOnly; SameSite=Strict; Max-Age="
                    + (jwtClaim.refreshTokenExpirationTime() * 60 * 60)
                    + "; "
                    + cookiesAttributesValues.secure()
                    + "; "
                    + cookiesAttributesValues.path()
                    + "="
                    + cookiesAttributesValues.pathValue());

            accessTokenAndRefreshToken = new AccessTokenAndRefreshTokenDTO();
            accessTokenAndRefreshToken.setAccessToken(accessTokenAndRefreshTokenAndFingerPrint.getAccessToken());
            accessTokenAndRefreshToken.setRefreshToken(accessTokenAndRefreshTokenAndFingerPrint.getRefreshToken());

            timeHandler.timingEqualization();

            return new ResponseEntity<>(accessTokenAndRefreshToken, headers, HttpStatus.PERMANENT_REDIRECT );
        } catch (EmailNotFoundException | EmailPatternNotValidException | VerificationTokenNotFoundException |
                 VerificationTokenPatternNotValidException | VerificationTokenDurationExpiredException |
                 VerificationTokensNotEqualsException | AccountNotFoundException | AccountIsActivatedException |
                 ValueNullException | AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException e) {
            timeHandler.timingEqualization();
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/candidate/default-method/reset-password/token/generate")
    public ResponseEntity<?> obtainResetPasswordUrlForAccountCreatedByDefaultMethod(@RequestBody String email){
        TimeHandler timeHandler = new TimeHandler(responseTime.responseTimeWithDispatching());
        timeHandler.start();

        System.out.println("email " +email);
        EmailAndUrlDTO emailAndUrlDTO;
        AccountEmailAndUrlDTO accountEmailAndUrlDTO;

        try {
            accountEmailAndUrlDTO = accountFacade.generateResetPasswordUrlForAccountCreatedByDefaultMethod(email);

            emailAndUrlDTO = new EmailAndUrlDTO(accountEmailAndUrlDTO.getEmail(), accountEmailAndUrlDTO.getUrl());
        } catch (UrlConfigurationNotFoundException e) {
//            throw new RuntimeException(e);
            timeHandler.timingEqualization();
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AccountNotFoundException | EmailNotFoundException | EmailPatternNotValidException
                 | VerificationTokenNotRegeneratedYetException e) {
            timeHandler.timingEqualization();
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
//            throw new RuntimeException(e);
//            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            emailFacade.sendURLResetPassword(emailAndUrlDTO);
            timeHandler.timingEqualization();
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
        } catch (UnprocessableEntityException | ForbiddenException | InternalServerErrorException |
                 UnexpectedException e) {
            timeHandler.timingEqualization();
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {

            try {
                authenticationFacade.authenticateAuthenticationServiceWithDefaultMethod();
                emailFacade.sendURLResetPassword(emailAndUrlDTO);
                timeHandler.timingEqualization();
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
            } catch (UnprocessableEntityException | ForbiddenException | UnauthorizedException |
                     InternalServerErrorException | UnexpectedException | ValueNullException |
                     AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException ex) {
                timeHandler.timingEqualization();
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    }

    @PostMapping("/candidate/default-method/reset-password/token/{token}")
    public ResponseEntity<?> resetPasswordForCandidateAccountWithDefaultMethod(@PathVariable String token, @RequestParam("email") String email, @RequestBody String newPassword){
        System.out.println("hi i m in reset Password");
        TimeHandler timeHandler = new TimeHandler(responseTime.responseTimeWithDispatching());
        timeHandler.start();

        HttpHeaders headers = new HttpHeaders();

        try {
            accountFacade.resetPasswordTheCandidateAccountCreatedByDefaultMethod(new AccountEmailAndNewPasswordAndActivationTokenDTO(email, newPassword, token));
        } catch (PasswordNotFoundException | EmailNotFoundException | EmailPatternNotValidException |
                 VerificationTokensNotEqualsException | VerificationTokenPatternNotValidException |
                 AccountNotFoundException | VerificationTokenDurationExpiredException |
                 PasswordPatternNotValidException | VerificationTokenNotFoundException e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>(headers,HttpStatus.NOT_FOUND);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        try {
            emailFacade.sendInformationOfPasswordReset(email);
            timeHandler.timingEqualization();
            return new ResponseEntity<>(headers,HttpStatus.OK);
        } catch (UnprocessableEntityException | ForbiddenException | UnexpectedException |
                 InternalServerErrorException e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>( new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            System.out.println(e.getMessage());
            try {
                authenticationFacade.authenticateAuthenticationServiceWithDefaultMethod();
                emailFacade.sendInformationOfPasswordReset(email);
                timeHandler.timingEqualization();
                return new ResponseEntity<>(headers,HttpStatus.OK);
            } catch (UnprocessableEntityException | ForbiddenException | InternalServerErrorException |
                     UnauthorizedException | UnexpectedException | ValueNullException |
                     AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException ex) {
                System.out.println(ex.getMessage());
                timeHandler.timingEqualization();
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/admin/role/read/all")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<?> readAllAdminRoles(){
        return new ResponseEntity<>(accountFacade.readAllAdminRoles(), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/admin/permission/read/all")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<?> readAllAdminPermissions(){
        return new ResponseEntity<>(accountFacade.readAllAdminPermissions(), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/admin/read/all")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<AdminAccountInformationResponseHttpList> readAllAdminProfileAndAccountInformation(){
        AdminAccountInformationResponseHttpList adminAccountInformationResponseHttpList = new AdminAccountInformationResponseHttpList(new ArrayList<>());
        List<AdminAccountInformationDTO> adminAccountInformationDTOList;

        try {
            adminAccountInformationDTOList = adminFacade.readAllAdmins();
            for (AdminAccountInformationDTO adminAccountInformation : adminAccountInformationDTOList){
                adminAccountInformationResponseHttpList.getAdminAccountInformationResponseHttpList().add(
                        new AdminAccountInformationResponseHttp(adminAccountInformation.getIdAccount(),
                                adminAccountInformation.getCreationDate(),
                                adminAccountInformation.getEmail(),
                                adminAccountInformation.isSuspended(),
                                adminAccountInformation.getFirstName(),
                                adminAccountInformation.getLastName(),
                                adminAccountInformation.getRegistration(),
                                adminAccountInformation.getRoles(),
                                adminAccountInformation.getPermissions()));
            }

            return new ResponseEntity<AdminAccountInformationResponseHttpList>(adminAccountInformationResponseHttpList, new HttpHeaders(), HttpStatus.OK);
        } catch (UnexpectedResponseBodyException | UnexpectedException e) {
            return new ResponseEntity<AdminAccountInformationResponseHttpList>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            System.out.println(e.getMessage());
            try {
                authenticationFacade.authenticateAuthenticationServiceWithDefaultMethod();
                adminAccountInformationDTOList = adminFacade.readAllAdmins();
                for (AdminAccountInformationDTO adminAccountInformation : adminAccountInformationDTOList){
                    adminAccountInformationResponseHttpList.getAdminAccountInformationResponseHttpList().add(
                            new AdminAccountInformationResponseHttp(adminAccountInformation.getIdAccount(),
                                    adminAccountInformation.getCreationDate(),
                                    adminAccountInformation.getEmail(),
                                    adminAccountInformation.isSuspended(),
                                    adminAccountInformation.getFirstName(),
                                    adminAccountInformation.getLastName(),
                                    adminAccountInformation.getRegistration(),
                                    adminAccountInformation.getRoles(),
                                    adminAccountInformation.getPermissions()));
                }

                return new ResponseEntity<AdminAccountInformationResponseHttpList>(adminAccountInformationResponseHttpList, new HttpHeaders(), HttpStatus.OK);
            } catch (ValueNullException | AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException |
                     UnexpectedResponseBodyException | UnexpectedException |
                     UnauthorizedException ex) {
                System.out.println(ex.getMessage());
                return new ResponseEntity<AdminAccountInformationResponseHttpList>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/admin/read/search/keywords")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<AdminAccountInformationResponseHttpList> readAdminsByKeywords(@RequestBody SearchWithKeywordsRequestHttp searchWithKeywordsRequestHttp){
        AdminAccountInformationResponseHttpList adminAccountInformationResponseHttpList = new AdminAccountInformationResponseHttpList(new ArrayList<>());
        List<AdminAccountInformationDTO> adminAccountInformationDTOList;

        System.out.println( "getKeywords" + searchWithKeywordsRequestHttp.getKeywords());
        try {
            adminAccountInformationDTOList = adminFacade.readAdminsByKeywords(searchWithKeywordsRequestHttp.getKeywords());
            for (AdminAccountInformationDTO adminAccountInformation : adminAccountInformationDTOList){
                adminAccountInformationResponseHttpList.getAdminAccountInformationResponseHttpList().add(
                        new AdminAccountInformationResponseHttp(adminAccountInformation.getIdAccount(),
                                adminAccountInformation.getCreationDate(),
                                adminAccountInformation.getEmail(),
                                adminAccountInformation.isSuspended(),
                                adminAccountInformation.getFirstName(),
                                adminAccountInformation.getLastName(),
                                adminAccountInformation.getRegistration(),
                                adminAccountInformation.getRoles(),
                                adminAccountInformation.getPermissions()));
            }

            return new ResponseEntity<AdminAccountInformationResponseHttpList>(adminAccountInformationResponseHttpList, new HttpHeaders(), HttpStatus.OK);
        } catch (UnexpectedResponseBodyException | UnexpectedException |
                 KeywordsNotFoundException e) {
            return new ResponseEntity<AdminAccountInformationResponseHttpList>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            System.out.println(e.getMessage());
            try {
                authenticationFacade.authenticateAuthenticationServiceWithDefaultMethod();
                adminAccountInformationDTOList = adminFacade.readAdminsByKeywords(searchWithKeywordsRequestHttp.getKeywords());
                for (AdminAccountInformationDTO adminAccountInformation : adminAccountInformationDTOList){
                    adminAccountInformationResponseHttpList.getAdminAccountInformationResponseHttpList().add(
                            new AdminAccountInformationResponseHttp(adminAccountInformation.getIdAccount(),
                                    adminAccountInformation.getCreationDate(),
                                    adminAccountInformation.getEmail(),
                                    adminAccountInformation.isSuspended(),
                                    adminAccountInformation.getFirstName(),
                                    adminAccountInformation.getLastName(),
                                    adminAccountInformation.getRegistration(),
                                    adminAccountInformation.getRoles(),
                                    adminAccountInformation.getPermissions()));
                }

                return new ResponseEntity<AdminAccountInformationResponseHttpList>(adminAccountInformationResponseHttpList, new HttpHeaders(), HttpStatus.OK);
            } catch (ValueNullException | AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException |
                     UnexpectedResponseBodyException | UnexpectedException |
                     UnauthorizedException | KeywordsNotFoundException ex) {
                System.out.println(ex.getMessage());
                return new ResponseEntity<AdminAccountInformationResponseHttpList>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/admin/read/search/suspension")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<AdminAccountInformationResponseHttpList> readAllSuspendedAdmins(){
        AdminAccountInformationResponseHttpList adminAccountInformationResponseHttpList = new AdminAccountInformationResponseHttpList(new ArrayList<>());
        List<AdminAccountInformationDTO> adminAccountInformationDTOList;
        try {
            adminAccountInformationDTOList = adminFacade.readAllSuspendedAdmins();

            for (AdminAccountInformationDTO adminAccountInformation : adminAccountInformationDTOList){
                adminAccountInformationResponseHttpList.getAdminAccountInformationResponseHttpList().add(
                        new AdminAccountInformationResponseHttp(adminAccountInformation.getIdAccount(),
                                adminAccountInformation.getCreationDate(),
                                adminAccountInformation.getEmail(),
                                adminAccountInformation.isSuspended(),
                                adminAccountInformation.getFirstName(),
                                adminAccountInformation.getLastName(),
                                adminAccountInformation.getRegistration(),
                                adminAccountInformation.getRoles(),
                                adminAccountInformation.getPermissions()));
            }

            return new ResponseEntity<>(adminAccountInformationResponseHttpList, new HttpHeaders(), HttpStatus.OK);
        } catch (UnexpectedResponseBodyException | UnexpectedException e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            System.out.println(e.getMessage());
            try {
                authenticationFacade.authenticateAuthenticationServiceWithDefaultMethod();
                adminAccountInformationDTOList = adminFacade.readAllSuspendedAdmins();

                for (AdminAccountInformationDTO adminAccountInformation : adminAccountInformationDTOList){
                    adminAccountInformationResponseHttpList.getAdminAccountInformationResponseHttpList().add(
                            new AdminAccountInformationResponseHttp(adminAccountInformation.getIdAccount(),
                                    adminAccountInformation.getCreationDate(),
                                    adminAccountInformation.getEmail(),
                                    adminAccountInformation.isSuspended(),
                                    adminAccountInformation.getFirstName(),
                                    adminAccountInformation.getLastName(),
                                    adminAccountInformation.getRegistration(),
                                    adminAccountInformation.getRoles(),
                                    adminAccountInformation.getPermissions()));
                }

                return new ResponseEntity<>(adminAccountInformationResponseHttpList, new HttpHeaders(), HttpStatus.OK);
            } catch (ValueNullException | AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException |
                     UnexpectedResponseBodyException | UnexpectedException |
                     UnauthorizedException ex) {
                System.out.println(ex.getMessage());
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (AdminsIdsNotFoundException exc){
                System.out.println(exc.getMessage());
                return new ResponseEntity<AdminAccountInformationResponseHttpList>(null, new HttpHeaders(), HttpStatus.OK);
            }
        } catch (AdminsIdsNotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<AdminAccountInformationResponseHttpList>(null, new HttpHeaders(), HttpStatus.OK);
        }  catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/admin/update/suspension")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<Void> updateAdminAccountSuspensionState(@RequestBody AdminSuspensionStateRequestHttp adminSuspensionStateRequestHttp){
        System.out.println(adminSuspensionStateRequestHttp.getSuspensionState());
        try {
            accountFacade.updateAdminAccountSuspensionState(adminSuspensionStateRequestHttp.getIdAccount(), adminSuspensionStateRequestHttp.getSuspensionState());
            return new ResponseEntity<Void>(new HttpHeaders(), HttpStatus.OK);
        } catch (AccountNotFoundException | ValueNullException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<Void>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<Void>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/admin/update/password")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<Void> updateAdminAccountPassword(@RequestBody AdminNewPasswordRequestHttp adminNewPasswordRequestHttp){
        try {
            accountFacade.updateAdminAccountPassword(adminNewPasswordRequestHttp.getIdAccount(), adminNewPasswordRequestHttp.getNewPassword());
            return new ResponseEntity<Void>(new HttpHeaders(), HttpStatus.OK);
        } catch (AccountNotFoundException | PasswordNotFoundException | PasswordPatternNotValidException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<Void>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (ValueNullException | AuthenticationTypeNullPointerException |
                 UnknownAuthenticationTypeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<Void>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/admin/update/information")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<AdminAccountInformationResponseHttp> updateAdmin(@RequestBody AdminEmailRolesPermissionsAndInformationRequestHttp adminRequest){
        AdminAccountInformationDTO adminAccountInformation;
        AdminAccountInformationResponseHttp adminAccountInformationResponseHttp;
        AdminPartialAccountInformationDTO adminPartialAccountInformationDTO = new AdminPartialAccountInformationDTO(adminRequest.getIdAccount(),
                adminRequest.getEmail(),
                adminRequest.getFirstName(),
                adminRequest.getLastName(),
                adminRequest.getRegistration(),
                adminRequest.getRoles(),
                adminRequest.getPermissions());
        try {
            adminAccountInformation = adminFacade.updateAdmin(adminPartialAccountInformationDTO);
            adminAccountInformationResponseHttp = new AdminAccountInformationResponseHttp(adminAccountInformation.getIdAccount(),
                    adminAccountInformation.getCreationDate(),
                    adminAccountInformation.getEmail(),
                    adminAccountInformation.isSuspended(),
                    adminAccountInformation.getFirstName(),
                    adminAccountInformation.getLastName(),
                    adminAccountInformation.getRegistration(),
                    adminAccountInformation.getRoles(),
                    adminAccountInformation.getPermissions());

            return new ResponseEntity<>(adminAccountInformationResponseHttp, new HttpHeaders(), HttpStatus.OK);
        } catch (UnexpectedException e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            System.out.println(e.getMessage());
            try {
                authenticationFacade.authenticateAuthenticationServiceWithDefaultMethod();
                adminAccountInformation = adminFacade.updateAdmin(adminPartialAccountInformationDTO);
                adminAccountInformationResponseHttp = new AdminAccountInformationResponseHttp(adminAccountInformation.getIdAccount(),
                        adminAccountInformation.getCreationDate(),
                        adminAccountInformation.getEmail(),
                        adminAccountInformation.isSuspended(),
                        adminAccountInformation.getFirstName(),
                        adminAccountInformation.getLastName(),
                        adminAccountInformation.getRegistration(),
                        adminAccountInformation.getRoles(),
                        adminAccountInformation.getPermissions());

                return new ResponseEntity<>(adminAccountInformationResponseHttp, new HttpHeaders(), HttpStatus.OK);
            } catch (AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException | UnexpectedException |
                     UnauthorizedException | RolePrefixException | RoleNotDefinedException | PermissionPrefixException |
                     PermissionNotDefinedException ex) {
                System.out.println(ex.getMessage());
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch(EmailNotFoundException | EmailPatternNotValidException | AccountAlreadyExistsException |
                    UserProfileInformationNotProvidedException | ValueNullException |
                    IdAccountNotFoundException | AccountNotFoundException ex){
                AdminAccountInformationResponseHttp accountResponse = new AdminAccountInformationResponseHttp();
                System.out.println(ex.getMessage());

                if (ex instanceof EmailNotFoundException || ex instanceof EmailPatternNotValidException ){
                    accountResponse.setEmail("Error1");
                    return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                } else if (ex instanceof AccountAlreadyExistsException){
                    accountResponse.setEmail("Exists");
                    return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                } else if(ex instanceof IdAccountNotFoundException){
                    accountResponse.setIdAccount(-1);
                    return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                } else if (ex instanceof  AccountNotFoundException){
                    accountResponse.setIdAccount(-2);
                    return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                } else {
                    if(ex.getMessage().contains("firstName")){
                        accountResponse.setFirstName("Error");
                        return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                    } else if (ex.getMessage().contains("lastName")) {
                        accountResponse.setLastName("Error");
                        return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                    } else if (ex.getMessage().contains("email")){
                        accountResponse.setEmail("Error2");
                        return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                    } else if (ex.getMessage().contains("registration")) {
                        accountResponse.setRegistration("Error");
                        return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                    } else if (ex.getMessage().contains("roles")) {
                        accountResponse.setRoles(new ArrayList<>());
                        return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
                    } else if(ex.getMessage().contains("permissions")) {
                        accountResponse.setPermissions(new ArrayList<>());
                        return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
                    } else {
                        System.out.println(ex.getMessage());
                        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            } catch (Exception ex){
                System.out.println(ex.getMessage());
                throw new RuntimeException(ex);
            }
        } catch (RolePrefixException | RoleNotDefinedException | PermissionPrefixException |
                 PermissionNotDefinedException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(EmailNotFoundException | EmailPatternNotValidException | AccountAlreadyExistsException |
                UserProfileInformationNotProvidedException | ValueNullException |
                IdAccountNotFoundException | AccountNotFoundException e){
            AdminAccountInformationResponseHttp accountResponse = new AdminAccountInformationResponseHttp();
            System.out.println(e.getMessage());

            if (e instanceof EmailNotFoundException || e instanceof EmailPatternNotValidException ){
                accountResponse.setEmail("Error1");
                return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            } else if (e instanceof AccountAlreadyExistsException){
                accountResponse.setEmail("Exists");
                return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            } else if(e instanceof IdAccountNotFoundException){
                accountResponse.setIdAccount(-1);
                return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            } else if (e instanceof  AccountNotFoundException){
                accountResponse.setIdAccount(-2);
                return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            } else {
                if(e.getMessage().contains("firstName")){
                    accountResponse.setFirstName("Error");
                    return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                } else if (e.getMessage().contains("lastName")) {
                    accountResponse.setLastName("Error");
                    return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                } else if (e.getMessage().contains("email")){
                    accountResponse.setEmail("Error2");
                    return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                } else if (e.getMessage().contains("registration")) {
                    accountResponse.setRegistration("Error");
                    return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                } else if (e.getMessage().contains("roles")) {
                    accountResponse.setRoles(new ArrayList<>());
                    return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
                } else {
                    accountResponse.setPermissions(new ArrayList<>());
                    return new ResponseEntity<>(accountResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/admin/delete/one")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<Void> deleteOneAdmin(@RequestBody AdminIdRequestHttp adminIdRequestHttp){
        try {
            adminFacade.deleteAdmin(adminIdRequestHttp.getId());
            return new ResponseEntity<Void>(new HttpHeaders(), HttpStatus.OK);
        } catch (AdminIdNotProvidedException | AccountNotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<Void>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnexpectedException | AdminNotDeletedException | UnexpectedResponseBodyException |
                 AdminNotCreatedException | UserProfileInformationNotProvidedException | IdAccountNotFoundException |
                 AdminsIdsNotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<Void>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            try {
                authenticationFacade.authenticateAuthenticationServiceWithDefaultMethod();
                adminFacade.deleteAdmin(adminIdRequestHttp.getId());
                return new ResponseEntity<Void>(new HttpHeaders(), HttpStatus.OK);
            } catch (AdminIdNotProvidedException | AccountNotFoundException | ValueNullException |
                     AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException ex) {
                System.out.println(ex.getMessage());
                return new ResponseEntity<Void>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (UnexpectedException | AdminNotDeletedException | UnexpectedResponseBodyException |
                     AdminNotCreatedException | UserProfileInformationNotProvidedException | IdAccountNotFoundException |
                     AdminsIdsNotFoundException | UnauthorizedException ex) {
                System.out.println(ex.getMessage());
                return new ResponseEntity<Void>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }
        }
    }

    /*@DeleteMapping("/admin/delete/group")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<Void> deleteGroupAdmin(AdminsIdsRequestHttp adminsIdsRequestHttp){
        return null;

    }

    @DeleteMapping("/admin/delete/all")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<Void> deleteOneAdmin(){
        return null;
    }*/



}
