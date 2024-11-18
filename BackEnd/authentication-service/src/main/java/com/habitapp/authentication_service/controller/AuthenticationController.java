package com.menara.authentication.controller;

import com.internship_hiring_menara.common.http.response.account.AuthenticationInformationResponseHttp;
import com.internship_hiring_menara.common.http.response.account.SuperAdminAuthenticationInformationResponseHttp;
import com.internship_hiring_menara.common.http.response.admin.AdminAuthenticationInformationResponseHttp;
import com.internship_hiring_menara.common.http.response.admin.AdminAuthenticationInformationResponseHttpList;
import com.internship_hiring_menara.common.http.response.authentication.AccessTokenAndFingerPrintResponseHttp;
import com.internship_hiring_menara.common.dto.service.ServiceCredentialCommonDTO;
import com.internship_hiring_menara.common.http.response.authentication.AccountAuthenticationInformationResponseHttpList;
import com.internship_hiring_menara.common.http.response.candidate.CandidateAuthenticationInformationResponseHttp;
import com.internship_hiring_menara.common.http.response.candidate.CandidateAuthenticationInformationResponseHttpList;
import com.menara.authentication.common.constant.CookiesNameConstants;
import com.menara.authentication.common.handler.time.TimeHandler;
import com.menara.authentication.configuration.record.CookiesAttributesValues;
import com.menara.authentication.configuration.record.HttpResponseTime;
import com.menara.authentication.configuration.record.JwtClaim;
import com.menara.authentication.domain.exception.account.*;
import com.menara.authentication.domain.exception.authentication.*;
import com.menara.authentication.domain.exception.general.ValueNullException;
import com.menara.authentication.domain.facade.AuthenticationFacade;
import com.menara.authentication.dto.account.SuperAdminAccountDTO;
import com.menara.authentication.dto.authentication.*;
import com.menara.authentication.dto.jwt.*;
import com.menara.authentication.dto.account.AccountDTO;
import com.menara.authentication.dto.service.ServiceCredentialDTO;
import com.menara.authentication.proxy.exception.common.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/authentication")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationFacade authenticationFacade;
    private CookiesAttributesValues cookiesAttributesValues;
    private JwtClaim jwtClaim;
    private HttpResponseTime responseTime;

    @PostMapping("/service/default")
    public ResponseEntity<?> authenticateServiceWithDefaultMethod(@RequestBody ServiceCredentialCommonDTO serviceCredentialCommonDTO, HttpServletRequest request) {
        TimeHandler timeHandler = new TimeHandler(responseTime.responseTimeWithoutDispatching());
        timeHandler.start();

        AccessTokenAndRefreshTokenAndFingerPrintDTO accessTokenAndRefreshTokenAndFingerPrint;
        AccessTokenAndFingerPrintDTO accessTokenAndFingerPrint;
        AccessTokenAndFingerPrintResponseHttp accessTokenAndFingerPrintCommon = new AccessTokenAndFingerPrintResponseHttp();
        HttpHeaders headers = new HttpHeaders();


        try {
            accessTokenAndRefreshTokenAndFingerPrint = authenticationFacade.authenticateServiceWithDefaultMethod(new ServiceCredentialDTO(serviceCredentialCommonDTO.getUsername(), serviceCredentialCommonDTO.getPassword()),
                    new JsonWebTokenConnectionInformationDTO(request.getRemoteAddr(), null, null));

            accessTokenAndFingerPrint = new AccessTokenAndFingerPrintDTO(accessTokenAndRefreshTokenAndFingerPrint.getAccessToken(), accessTokenAndRefreshTokenAndFingerPrint.getFingerPrint());

            accessTokenAndFingerPrintCommon.setAccessToken(accessTokenAndFingerPrint.getAccessToken());
            accessTokenAndFingerPrintCommon.setFingerPrint(accessTokenAndFingerPrint.getFingerPrint());

            timeHandler.timingEqualization();
            return new ResponseEntity<>(accessTokenAndFingerPrintCommon, headers, HttpStatus.OK);
        } catch (InstanceOfException | ValueNullException | AuthenticationTypeNullPointerException |
                 UnknownAuthenticationTypeException e) {
            timeHandler.timingEqualization();
            System.out.println(e.getMessage());
            return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            throw e;
        }
    }

    @PostMapping("/super-admin/default")
    public ResponseEntity<?> authenticateSuperAdminWithDefaultMethod(@RequestBody SuperAdminAccountDTO account, String screenResolution, HttpServletRequest request){
        TimeHandler timeHandler = new TimeHandler(responseTime.responseTimeWithoutDispatching());
        timeHandler.start();

        JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation = new JsonWebTokenConnectionInformationDTO();

        String userAgent = request.getHeader("User-Agent");

        jsonWebTokenConnectionInformation.setUserAgent(userAgent != null ? userAgent : "unknown");
        jsonWebTokenConnectionInformation.setIp(request.getRemoteAddr());
        jsonWebTokenConnectionInformation.setScreenResolution(screenResolution);

        AccessTokenAndRefreshTokenDTO accessTokenAndRefreshToken;
        AccessTokenAndRefreshTokenAndFingerPrintDTO accessTokenAndRefreshTokenAndFingerPrint;
        HttpHeaders headers = new HttpHeaders();

        try {
            accessTokenAndRefreshTokenAndFingerPrint = authenticationFacade.authenticateSuperAdminWithDefaultMethod(account, jsonWebTokenConnectionInformation);
            accessTokenAndRefreshToken = this.setFingerprintCookieAndGetAccessTokenRefreshToken(headers, accessTokenAndRefreshTokenAndFingerPrint);

            timeHandler.timingEqualization();
            return new ResponseEntity<>(accessTokenAndRefreshToken, headers, HttpStatus.OK);
        } catch (ValueNullException | InstanceOfException | AuthenticationTypeNullPointerException |
                 UnknownAuthenticationTypeException e) {
            timeHandler.timingEqualization();
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(Exception e){
            timeHandler.timingEqualization();
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @PostMapping("/admin/default")
    public ResponseEntity<?> authenticateAdminWithDefaultMethod(@RequestBody AccountDTO account, String screenResolution, HttpServletRequest request){
        TimeHandler timeHandler = new TimeHandler(responseTime.responseTimeWithoutDispatching());
        timeHandler.start();

        JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation = new JsonWebTokenConnectionInformationDTO();

        String userAgent = request.getHeader("User-Agent");

        jsonWebTokenConnectionInformation.setUserAgent(userAgent != null ? userAgent : "unknown");
        jsonWebTokenConnectionInformation.setIp(request.getRemoteAddr());
        jsonWebTokenConnectionInformation.setScreenResolution(screenResolution);

        AccessTokenAndRefreshTokenDTO accessTokenAndRefreshToken;
        AccessTokenAndRefreshTokenAndFingerPrintDTO accessTokenAndRefreshTokenAndFingerPrint;
        HttpHeaders headers = new HttpHeaders();

        try {
            System.out.println("hi authentication admin");

            accessTokenAndRefreshTokenAndFingerPrint = authenticationFacade.authenticateAdminWithDefaultMethod(account, jsonWebTokenConnectionInformation);
            accessTokenAndRefreshToken = this.setFingerprintCookieAndGetAccessTokenRefreshToken(headers, accessTokenAndRefreshTokenAndFingerPrint);

            timeHandler.timingEqualization();
            return new ResponseEntity<>(accessTokenAndRefreshToken, headers, HttpStatus.OK);
        } catch (ValueNullException | InstanceOfException | AuthenticationTypeNullPointerException |
                 UnknownAuthenticationTypeException e) {
            System.out.println("hi authentication admin UNAUTHORIZED");

            timeHandler.timingEqualization();
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AccountSuspendedException e) {
            System.out.println("hi authentication admin UNAUTHORIZED");

            timeHandler.timingEqualization();
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        } catch(Exception e){
            System.out.println("hi authentication admin UNAUTHORIZED");

            timeHandler.timingEqualization();
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @PostMapping("/candidate/default")
    public ResponseEntity<?> authenticateCandidateWithDefaultMethod(@RequestBody AccountDTO account, String screenResolution, HttpServletRequest request) {
        TimeHandler timeHandler = new TimeHandler(responseTime.responseTimeWithoutDispatching());
        timeHandler.start();

        JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation = new JsonWebTokenConnectionInformationDTO();

        String userAgent = request.getHeader("User-Agent");

        jsonWebTokenConnectionInformation.setUserAgent(userAgent != null ? userAgent : "unknown");
        jsonWebTokenConnectionInformation.setIp(request.getRemoteAddr());
        jsonWebTokenConnectionInformation.setScreenResolution(screenResolution);



        AccessTokenAndRefreshTokenDTO accessTokenAndRefreshToken;
        AccessTokenAndRefreshTokenAndFingerPrintDTO accessTokenAndRefreshTokenAndFingerPrint;
        HttpHeaders headers = new HttpHeaders();
        try {
            accessTokenAndRefreshTokenAndFingerPrint = authenticationFacade.authenticateCandidateWithDefaultMethod(account, jsonWebTokenConnectionInformation);
            accessTokenAndRefreshToken = this.setFingerprintCookieAndGetAccessTokenRefreshToken(headers, accessTokenAndRefreshTokenAndFingerPrint);

            timeHandler.timingEqualization();
            return new ResponseEntity<>(accessTokenAndRefreshToken, headers, HttpStatus.OK);
        } catch (InstanceOfException | ValueNullException | AuthenticationTypeNullPointerException |
                 UnknownAuthenticationTypeException e) {
            timeHandler.timingEqualization();
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AccountActivationException e) {
            timeHandler.timingEqualization();
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        } catch(Exception e){
            timeHandler.timingEqualization();
            throw e;
        }
    }

    @GetMapping("/candidate/google")
    public ResponseEntity<Void> googleAuthentication(){
        System.out.println("hi im in google authentication");
        String url = authenticationFacade.obtainGoogleAuthenticationUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));

        return new  ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/candidate/google/callback")
    public ResponseEntity<?> authenticateCandidateWithGoogleMethod(@RequestParam("code") String code, String screenResolution, HttpServletRequest request) {
        System.out.println( "hi this is code " + code);

        JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation = new JsonWebTokenConnectionInformationDTO();

        String userAgent = request.getHeader("User-Agent");

        jsonWebTokenConnectionInformation.setUserAgent(userAgent != null ? userAgent : "unknown");
        jsonWebTokenConnectionInformation.setIp(request.getRemoteAddr());
        jsonWebTokenConnectionInformation.setScreenResolution(screenResolution);

        AccessTokenAndRefreshTokenDTO accessTokenAndRefreshToken;
        AccessTokenAndRefreshTokenAndFingerPrintDTO accessTokenAndRefreshTokenAndFingerPrint;
        HttpHeaders headers = new HttpHeaders();

        try {
            accessTokenAndRefreshTokenAndFingerPrint = authenticationFacade.authenticateCandidateWithGoogleMethod(code, jsonWebTokenConnectionInformation);
            accessTokenAndRefreshToken = this.setFingerprintCookieAndGetAccessTokenRefreshToken(headers, accessTokenAndRefreshTokenAndFingerPrint);
//            System.out.println(headers.get(HttpHeaders.SET_COOKIE).get(0));

            return new ResponseEntity<>(accessTokenAndRefreshToken, headers, HttpStatus.OK);
        } catch (GeneralSecurityException | IOException | GoogleIdTokenNotValidException | InstanceOfException |
                 EmailNotFoundException | EmailPatternNotValidException | RolePrefixException |
                 RoleNotDefinedException | PermissionPrefixException | AccountNotCreatedException |
                 PermissionNotDefinedException | RoleNotFoundException | UnexpectedException | UnauthorizedException |
                 AccountAlreadyExistsException | ValueNullException | AuthenticationTypeNullPointerException |
                 UnknownAuthenticationTypeException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
            System.out.println(e.getMessage());
            return new ResponseEntity<String>( new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }
//        catch (Exception e){
//            System.out.println(e.getClass());
//            return new ResponseEntity<String>( new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
//            throw new RuntimeException(e);
//        }
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<AccessTokenAndRefreshTokenDTO>  authenticateWithRefreshToken(@RequestBody String refreshToken, HttpServletRequest request){
        JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation = new JsonWebTokenConnectionInformationDTO();
        String userAgent = request.getHeader("User-Agent");
        String fingerprint = null;

        for (Cookie cookie : request.getCookies()){
            if(cookie.getName().equals(CookiesNameConstants.TOKEN_FINGERPRINT)){
                fingerprint = cookie.getValue();
            }
        }
        System.out.println(fingerprint);

        jsonWebTokenConnectionInformation.setUserAgent(userAgent != null ? userAgent : "unknown");
        jsonWebTokenConnectionInformation.setIp(request.getRemoteAddr());

        AccessTokenAndRefreshTokenDTO accessTokenAndRefreshToken;
        AccessTokenAndRefreshTokenAndFingerPrintDTO accessTokenAndRefreshTokenAndFingerPrint;
        HttpHeaders headers = new HttpHeaders();

        try {
            accessTokenAndRefreshTokenAndFingerPrint = authenticationFacade.authenticateWithRefreshToken(refreshToken, fingerprint, jsonWebTokenConnectionInformation);
            accessTokenAndRefreshToken = this.setFingerprintCookieAndGetAccessTokenRefreshToken(headers, accessTokenAndRefreshTokenAndFingerPrint);
            return new ResponseEntity<>(accessTokenAndRefreshToken, headers, HttpStatus.OK);
        } catch (AccountSuspendedException | ValueNullException | InstanceOfException | UnknownAccountTypeException |
                 AuthenticationTypeNullPointerException | AccountActivationException |
                 UnprocessableAccountTypeException | UnknownAuthenticationTypeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
        }
    }

    /*@PostMapping("/revoke/jwt/mine/current")
    public ResponseEntity<?> revokeMyJwt(@AuthenticationPrincipal Jwt jwt){
        JsonWebTokenJtiAndDateExpirationDTO jtiAndDateExpiration = new JsonWebTokenJtiAndDateExpirationDTO(jwt.getId(), jwt.getExpiresAt());

        authenticationFacade.revokeJsonWebToken(jtiAndDateExpiration);

        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
    }*/

    // role only services
    @PostMapping("/revoke/jwt/verify")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SERVER)")
    public ResponseEntity<?> jwtIsRevoked(@RequestBody String jti){

        HttpHeaders headers = new HttpHeaders();

        if (authenticationFacade.jwtIsRevoked(jti)){
            return new ResponseEntity<Boolean>(true, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<Boolean>(false, headers, HttpStatus.OK);
        }
    }

    @GetMapping("/account/read/all")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<AccountAuthenticationInformationResponseHttpList> getAllAccountAuthentications(){
        AuthenticationAccountDTO authenticationAccountDTO;
        AccountAuthenticationInformationResponseHttpList authenticationInformationResponseHttpList = new AccountAuthenticationInformationResponseHttpList(new ArrayList<>(), new ArrayList<>());
        try {
            authenticationAccountDTO = authenticationFacade.getAllAccountAuthentications();

            for (CandidateAuthenticationInformationDTO candidate : authenticationAccountDTO.getCandidateAuthenticationInformationList()){
                authenticationInformationResponseHttpList.getCandidateAuthenticationInformationResponseHttpList().add(
                        new CandidateAuthenticationInformationResponseHttp(candidate.getIdAccount(),
                                candidate.getFirstName(),
                                candidate.getLastName(),
                                candidate.getEmail(),
                                candidate.getImageUrl(),
                                candidate.getGender(),
                                candidate.getLastIpConnection(),
                                candidate.getLastConnection(),
                                candidate.getUserAgent(),
                                candidate.getScreenResolution(),
                                candidate.getUserType()));
            }

            for (AdminAuthenticationInformationDTO admin : authenticationAccountDTO.getAdminAuthenticationInformationList()){
                authenticationInformationResponseHttpList.getAdminAuthenticationInformationResponseHttpList().add(
                        new AdminAuthenticationInformationResponseHttp(admin.getIdAccount(),
                                admin.getFirstName(),
                                admin.getLastName(),
                                admin.getEmail(),
                                admin.getRegistration(),
                                admin.getLastIpConnection(),
                                admin.getLastConnection(),
                                admin.getUserAgent(),
                                admin.getScreenResolution(),
                                admin.getUserType()));
            }


            return new ResponseEntity<AccountAuthenticationInformationResponseHttpList>(authenticationInformationResponseHttpList, new HttpHeaders(), HttpStatus.OK);
        } catch (UnexpectedResponseBodyException | UnexpectedException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            try {
                authenticationFacade.authenticateAuthenticationServiceWithDefaultMethod();
                authenticationAccountDTO = authenticationFacade.getAllAccountAuthentications();

                for (CandidateAuthenticationInformationDTO candidate : authenticationAccountDTO.getCandidateAuthenticationInformationList()){
                    authenticationInformationResponseHttpList.getCandidateAuthenticationInformationResponseHttpList().add(
                            new CandidateAuthenticationInformationResponseHttp(candidate.getIdAccount(),
                                    candidate.getFirstName(),
                                    candidate.getLastName(),
                                    candidate.getEmail(),
                                    candidate.getImageUrl(),
                                    candidate.getGender(),
                                    candidate.getLastIpConnection(),
                                    candidate.getLastConnection(),
                                    candidate.getUserAgent(),
                                    candidate.getScreenResolution(),
                                    candidate.getUserType()));
                }

                for (AdminAuthenticationInformationDTO admin : authenticationAccountDTO.getAdminAuthenticationInformationList()){
                    authenticationInformationResponseHttpList.getAdminAuthenticationInformationResponseHttpList().add(
                            new AdminAuthenticationInformationResponseHttp(admin.getIdAccount(),
                                    admin.getFirstName(),
                                    admin.getLastName(),
                                    admin.getEmail(),
                                    admin.getRegistration(),
                                    admin.getLastIpConnection(),
                                    admin.getLastConnection(),
                                    admin.getUserAgent(),
                                    admin.getScreenResolution(),
                                    admin.getUserType()));
                }

                return new ResponseEntity<AccountAuthenticationInformationResponseHttpList>(authenticationInformationResponseHttpList, new HttpHeaders(), HttpStatus.OK);
            } catch (ValueNullException | AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException |
                     UnexpectedResponseBodyException | UnexpectedException | UnauthorizedException ex) {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    // new featers

    @GetMapping("/admin/read/all")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<AdminAuthenticationInformationResponseHttpList> getAllAdminAuthentications(){
        List<AdminAuthenticationInformationDTO> adminAuthenticationInformationDTOList;
        AdminAuthenticationInformationResponseHttpList adminAuthenticationInformationResponseHttpList = new AdminAuthenticationInformationResponseHttpList(new ArrayList<>());
        try {
            adminAuthenticationInformationDTOList = authenticationFacade.getAllAdminAuthentications();

            for (AdminAuthenticationInformationDTO admin : adminAuthenticationInformationDTOList){
                adminAuthenticationInformationResponseHttpList.getAdminAuthenticationInformationResponseHttp().add(
                        new AdminAuthenticationInformationResponseHttp(admin.getIdAccount(),
                                admin.getFirstName(),
                                admin.getLastName(),
                                admin.getEmail(),
                                admin.getRegistration(),
                                admin.getLastIpConnection(),
                                admin.getLastConnection(),
                                admin.getUserAgent(),
                                admin.getScreenResolution(),
                                admin.getUserType()));
            }


            return new ResponseEntity<AdminAuthenticationInformationResponseHttpList>(adminAuthenticationInformationResponseHttpList, new HttpHeaders(), HttpStatus.OK);
        } catch (UnexpectedResponseBodyException | UnexpectedException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            try {
                authenticationFacade.authenticateAuthenticationServiceWithDefaultMethod();
                adminAuthenticationInformationDTOList = authenticationFacade.getAllAdminAuthentications();

                for (AdminAuthenticationInformationDTO admin : adminAuthenticationInformationDTOList){
                    adminAuthenticationInformationResponseHttpList.getAdminAuthenticationInformationResponseHttp().add(
                            new AdminAuthenticationInformationResponseHttp(admin.getIdAccount(),
                                    admin.getFirstName(),
                                    admin.getLastName(),
                                    admin.getEmail(),
                                    admin.getRegistration(),
                                    admin.getLastIpConnection(),
                                    admin.getLastConnection(),
                                    admin.getUserAgent(),
                                    admin.getScreenResolution(),
                                    admin.getUserType()));
                }

                return new ResponseEntity<AdminAuthenticationInformationResponseHttpList>(adminAuthenticationInformationResponseHttpList, new HttpHeaders(), HttpStatus.OK);
            } catch (ValueNullException | AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException |
                     UnexpectedResponseBodyException | UnexpectedException | UnauthorizedException ex) {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/candidate/read/all")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<CandidateAuthenticationInformationResponseHttpList> getAllCandidateAuthentications(){
        List<CandidateAuthenticationInformationDTO> candidateAuthenticationInformationDTOList;
        CandidateAuthenticationInformationResponseHttpList candidateAuthenticationInformationResponseHttpList = new CandidateAuthenticationInformationResponseHttpList(new ArrayList<>());
        try {
            candidateAuthenticationInformationDTOList = authenticationFacade.getAllCandidateAuthentications();

            for (CandidateAuthenticationInformationDTO candidate : candidateAuthenticationInformationDTOList){
                candidateAuthenticationInformationResponseHttpList.getCandidateAuthenticationInformationResponseHttpList().add(
                        new CandidateAuthenticationInformationResponseHttp(candidate.getIdAccount(),
                                candidate.getFirstName(),
                                candidate.getLastName(),
                                candidate.getEmail(),
                                candidate.getImageUrl(),
                                candidate.getGender(),
                                candidate.getLastIpConnection(),
                                candidate.getLastConnection(),
                                candidate.getUserAgent(),
                                candidate.getScreenResolution(),
                                candidate.getUserType()));
            }


            return new ResponseEntity<CandidateAuthenticationInformationResponseHttpList>(candidateAuthenticationInformationResponseHttpList, new HttpHeaders(), HttpStatus.OK);
        } catch (UnexpectedResponseBodyException | UnexpectedException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            try {
                authenticationFacade.authenticateAuthenticationServiceWithDefaultMethod();
                candidateAuthenticationInformationDTOList = authenticationFacade.getAllCandidateAuthentications();

                for (CandidateAuthenticationInformationDTO candidate : candidateAuthenticationInformationDTOList){
                    candidateAuthenticationInformationResponseHttpList.getCandidateAuthenticationInformationResponseHttpList().add(
                            new CandidateAuthenticationInformationResponseHttp(candidate.getIdAccount(),
                                    candidate.getFirstName(),
                                    candidate.getLastName(),
                                    candidate.getEmail(),
                                    candidate.getImageUrl(),
                                    candidate.getGender(),
                                    candidate.getLastIpConnection(),
                                    candidate.getLastConnection(),
                                    candidate.getUserAgent(),
                                    candidate.getScreenResolution(),
                                    candidate.getUserType()));
                }

                return new ResponseEntity<CandidateAuthenticationInformationResponseHttpList>(candidateAuthenticationInformationResponseHttpList, new HttpHeaders(), HttpStatus.OK);
            } catch (ValueNullException | AuthenticationTypeNullPointerException | UnknownAuthenticationTypeException |
                     UnexpectedResponseBodyException | UnexpectedException | UnauthorizedException ex) {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/account/read/one")
    @PreAuthorize("hasAnyRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).CANDIDATE,  " +
            "T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).INTERNSHIP_ADMIN)")
    public ResponseEntity<AuthenticationInformationResponseHttp> getOneAccountAuthentications(@AuthenticationPrincipal Jwt jwt){
        try {
            AuthenticationInformationResponseHttp responseHttp;

            AuthenticationInformationDTO authenticationInformationDTO = authenticationFacade.getOneAccountAuthentications(Integer.parseInt(jwt.getSubject()));
            if (authenticationInformationDTO == null){
                return new ResponseEntity<AuthenticationInformationResponseHttp>(null, new HttpHeaders(), HttpStatus.OK);
            }

            responseHttp = new AuthenticationInformationResponseHttp(authenticationInformationDTO.getLastIpConnection(),
                    authenticationInformationDTO.getLastConnection(),
                    authenticationInformationDTO.getUserAgent(),
                    authenticationInformationDTO.getUserType());

            return new ResponseEntity<AuthenticationInformationResponseHttp>(responseHttp, new HttpHeaders(), HttpStatus.OK);
        } catch (NumberFormatException e){
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/super-admin/read/one")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).SUPER_ADMIN)")
    public ResponseEntity<SuperAdminAuthenticationInformationResponseHttp> getOneAccountAuthentications(){
        try {
            SuperAdminAuthenticationInformationResponseHttp responseHttp;

            SuperAdminAuthenticationInformationDTO superAdminAuthentication = authenticationFacade.getSuperAdminAuthentication();
            if (superAdminAuthentication == null){
                return new ResponseEntity<SuperAdminAuthenticationInformationResponseHttp>(null, new HttpHeaders(), HttpStatus.OK);
            }

            responseHttp = new SuperAdminAuthenticationInformationResponseHttp(superAdminAuthentication.getLastIpConnection(),
                    superAdminAuthentication.getLastConnection(),
                    superAdminAuthentication.getUserAgent());

            return new ResponseEntity<SuperAdminAuthenticationInformationResponseHttp>(responseHttp, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /*//filter for check if the jti is appartienne to je user or not
    @GetMapping("/connections/mine")
    public ResponseEntity<?> getAllMyConnections(@AuthenticationPrincipal Jwt jwt){

    }

    //filter for check if the jti is appartienne to je user or not
    @PostMapping("/revoke/jwt/mine/other")
    public ResponseEntity<?> revokeLotOfMyJwt(@AuthenticationPrincipal Jwt jwt, @RequestBody String[] jti){

    }

    // super admin roles
    @PostMapping("/connections/account/one")
    public ResponseEntity<?> getOneAccountConnections(long idAccount) {

    }

    // super admin roles
    @GetMapping("/connections/account/all")
    public ResponseEntity<?> getAllAccountConnections(){

    }

    // role super admin
    @PostMapping("/revoke/jwt/other")
    public ResponseEntity<?> revokeLotOfAccountJwt(@RequestBody String[] jtiList){

    }

    // super admin roles
    @GetMapping("/connections/service/all")
    public ResponseEntity<?> getAllServiceConnections(){

    }

    // super admin roles
    @PostMapping("/revoke/jwt/other")
    public ResponseEntity<?> revokeLotOfServiceJwt(@RequestBody String[] jtiList){

    }

    // super admin roles
    @PostMapping("/revoke/jwt/other")
    public ResponseEntity<?> getAllAuthentication(){

    }
*/

    private AccessTokenAndRefreshTokenDTO setFingerprintCookieAndGetAccessTokenRefreshToken(HttpHeaders headers,
            AccessTokenAndRefreshTokenAndFingerPrintDTO accessTokenAndRefreshTokenAndFingerPrint) {
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

        System.out.println(accessTokenAndRefreshTokenAndFingerPrint.getFingerPrint());

        AccessTokenAndRefreshTokenDTO accessTokenAndRefreshToken = new AccessTokenAndRefreshTokenDTO();
        accessTokenAndRefreshToken.setRefreshToken(accessTokenAndRefreshTokenAndFingerPrint.getRefreshToken());
        accessTokenAndRefreshToken.setAccessToken(accessTokenAndRefreshTokenAndFingerPrint.getAccessToken());
        return accessTokenAndRefreshToken;
    }
}
