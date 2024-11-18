package com.menara.authentication.domain.service.imp;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.internship_hiring_menara.common.common.account.RoleNameCommonConstants;
import com.internship_hiring_menara.common.common.account.ServiceIdCommonConstants;
import com.menara.authentication.annotation.Instance;
import com.menara.authentication.common.constant.*;
import com.menara.authentication.configuration.record.FrontEndURL;
import com.menara.authentication.configuration.record.GoogleOAuth2Credential;
import com.menara.authentication.configuration.record.JwtClaim;
import com.menara.authentication.domain.entity.*;
import com.menara.authentication.domain.exception.account.AccountSuspendedException;
import com.menara.authentication.domain.exception.authentication.*;
import com.menara.authentication.domain.exception.general.ValueNullException;
import com.menara.authentication.domain.repository.*;
import com.menara.authentication.domain.service.AuthenticationService;
import com.menara.authentication.common.utlil.cryptography.hash.HashUtil;
import com.menara.authentication.common.utlil.generator.id.GenerateUniqueIdUtil;
import com.menara.authentication.common.utlil.generator.random.token.RandomTokenGeneratorUtil;
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
import com.menara.authentication.security.exception.filter.CookieFingerprintNotEqualWithJwtClaimException;
import com.menara.authentication.security.exception.filter.CookieTokenFingerprintNotFoundException;
import com.menara.authentication.security.userdetails.*;
import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImp implements AuthenticationService {
    private final UserDetailsService superAdminDefaultAccountRefreshTokenUserDetailsService;
    private final UserDetailsService adminDefaultAccountRefreshTokenUserDetailsService;
    private final UserDetailsService candidateDefaultAccountRefreshTokenUserDetailsService;
    private final UserDetailsService candidateGoogleAccountRefreshTokenUserDetailsService;
    private final AuthenticationManager candidateDefaultMthodAuthenticationManager;
    private final AuthenticationManager candidateGoogleMethodAuthenticationManger;
    private final AuthenticationManager adminDefaultMthodAuthenticationManager;
    private final AuthenticationManager serviceDefaultMthodAuthenticationManager;
    private final AuthenticationManager superAdminDefaultMethodAuthenticationManager;
    private final AuthenticationManager authenticationManagerRefreshToken;
    private final AccountAuthenticationRepository accountAuthenticationRepository;
    private final SuperAdminAuthenticationRepository superAdminAuthenticationRepository;
    private final RevokedJsonWebTokenRepository revokedJsonWebTokenRepository;
    private final AccountJsonWebTokenRepository accountJsonWebTokenRepository;
    private final ServiceJsonWebTokenRepository serviceJsonWebTokenRepository;
    private final SuperAdminJsonWebTokenRepository superAdminJsonWebTokenRepository;
    private final JwtEncoder accessJwtEncoder;
    private final JwtEncoder refreshJwtEncoder;
    private final JwtDecoder refreshjwtDecoder;
    private final JwtClaim jwtClaim;
    private final HashUtil hashUtil;
    private final GenerateUniqueIdUtil generateUniqueIdUtil;
    private final RandomTokenGeneratorUtil randomTokenGeneratorUtil;
    private final GoogleOAuth2Credential googleOAuth2Credential;
    private final FrontEndURL frontEndURL;

    public AuthenticationServiceImp(@Instance("RefreshTokenDefaultAccountSuperAdminDetails") UserDetailsService superAdminDefaultAccountRefreshTokenUserDetailsService,
                                    @Instance("RefreshTokenDefaultAccountAdminDetails") UserDetailsService adminDefaultAccountRefreshTokenUserDetailsService,
                                    @Instance("RefreshTokenDefaultAccountCandidateDetails") UserDetailsService candidateDefaultAccountRefreshTokenUserDetailsService,
                                    @Instance("RefreshTokenGoogleAccountCandidateDetails") UserDetailsService candidateGoogleAccountRefreshTokenUserDetailsService,
                                    @Instance("DefaultAccountCandidate") AuthenticationManager candidateDefaultMthodAuthenticationManager,
                                    @Instance("GoogleAccountCandidate") AuthenticationManager candidateGoogleMethodAuthenticationManger,
                                    @Instance("DefaultAccountAdmin") AuthenticationManager adminDefaultMthodAuthenticationManager,
                                    @Instance("DefaultAccountService") AuthenticationManager serviceDefaultMthodAuthenticationManager,
                                    @Instance("DefaultAccountSuperAdmin") AuthenticationManager superAdminDefaultMethodAuthenticationManager,
                                    @Instance("RefreshToken") AuthenticationManager authenticationManagerRefreshToken,
                                    AccountAuthenticationRepository accountAuthenticationRepository,
                                    SuperAdminAuthenticationRepository superAdminAuthenticationRepository,
                                    JwtClaim jwtClaim,
                                    HashUtil hashUtil,
                                    GenerateUniqueIdUtil generateUniqueIdUtil,
                                    RandomTokenGeneratorUtil randomTokenGeneratorUtil,
                                    GoogleOAuth2Credential googleOAuth2Credential,
                                    FrontEndURL frontEndURL,
                                    RevokedJsonWebTokenRepository revokedJsonWebTokenRepository,
                                    AccountJsonWebTokenRepository accountJsonWebTokenRepository,
                                    ServiceJsonWebTokenRepository serviceJsonWebTokenRepository,
                                    SuperAdminJsonWebTokenRepository superAdminJsonWebTokenRepository,
                                    @Instance("accessJwtEncoder") JwtEncoder accessJwtEncoder,
                                    @Instance("refreshJwtEncoder") JwtEncoder refrshJwtEncoder,
                                    @Instance("refreshJwtDecoder") JwtDecoder refrshjwtDecoder) {
        this.superAdminDefaultAccountRefreshTokenUserDetailsService = superAdminDefaultAccountRefreshTokenUserDetailsService;
        this.adminDefaultAccountRefreshTokenUserDetailsService = adminDefaultAccountRefreshTokenUserDetailsService;
        this.candidateDefaultAccountRefreshTokenUserDetailsService = candidateDefaultAccountRefreshTokenUserDetailsService;
        this.candidateGoogleAccountRefreshTokenUserDetailsService = candidateGoogleAccountRefreshTokenUserDetailsService;
        this.candidateDefaultMthodAuthenticationManager = candidateDefaultMthodAuthenticationManager;
        this.superAdminDefaultMethodAuthenticationManager = superAdminDefaultMethodAuthenticationManager;
        this.authenticationManagerRefreshToken = authenticationManagerRefreshToken;
        this.accountAuthenticationRepository = accountAuthenticationRepository;
        this.superAdminAuthenticationRepository = superAdminAuthenticationRepository;
        this.serviceJsonWebTokenRepository = serviceJsonWebTokenRepository;
        this.superAdminJsonWebTokenRepository = superAdminJsonWebTokenRepository;
        this.accessJwtEncoder = accessJwtEncoder;
        this.refreshJwtEncoder = refrshJwtEncoder;
        this.refreshjwtDecoder = refrshjwtDecoder;
        System.out.println(this.candidateDefaultMthodAuthenticationManager.toString());
        this.candidateGoogleMethodAuthenticationManger = candidateGoogleMethodAuthenticationManger;
        System.out.println(this.candidateGoogleMethodAuthenticationManger.toString());
        this.adminDefaultMthodAuthenticationManager = adminDefaultMthodAuthenticationManager;
        System.out.println(this.adminDefaultMthodAuthenticationManager.toString());
        this.serviceDefaultMthodAuthenticationManager = serviceDefaultMthodAuthenticationManager;
        System.out.println(this.serviceDefaultMthodAuthenticationManager.toString());
        this.jwtClaim = jwtClaim;
        this.hashUtil = hashUtil;
        this.generateUniqueIdUtil = generateUniqueIdUtil;
        this.randomTokenGeneratorUtil = randomTokenGeneratorUtil;
        this.googleOAuth2Credential = googleOAuth2Credential;
        this.frontEndURL = frontEndURL;
        this.revokedJsonWebTokenRepository = revokedJsonWebTokenRepository;
        this.accountJsonWebTokenRepository = accountJsonWebTokenRepository;
    }

    @Override
    public AccessTokenAndFingerPrintDTO authenticateAuthenticationServiceWithDefaultMethod() throws ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException {
        AccessTokenAndFingerPrintDTO jwtToken;
        AccessTokenAndRefreshTokenAndFingerPrintDTO tokenHolder;
        String authorities;

        jwtToken = new AccessTokenAndFingerPrintDTO();
        authorities = RoleNameCommonConstants.AUTHENTICATION_SERVER;

        String ip;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ip = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        tokenHolder = this.processGenerationToken(ServiceIdCommonConstants.AUTHENTICATION_SERVICE, authorities, false, new JsonWebTokenConnectionInformationDTO(ip, null, null), AuthenticationTypeConstants.SERVICE_JSON_WEB_TOKEN, AccountTypeConstants.SERVICE_DEFAULT_ACCOUNT, null);

        jwtToken.setAccessToken(tokenHolder.getAccessToken());
        jwtToken.setFingerPrint(tokenHolder.getFingerPrint());

        System.out.println(jwtToken.getFingerPrint());
        System.out.println(jwtToken.getAccessToken());

        return jwtToken;
    }

    @Override
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateServiceWithDefaultMethod(ServiceCredentialDTO credential, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException {
        Object user;
        ServiceDefaultMethod service;
        String authorities;

        Authentication authentication = serviceDefaultMthodAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credential.getUsername(), credential.getPassword())
        );

        if (!authentication.isAuthenticated()){
            throw new BadCredentialsException("username or password incorrect");
        }

        user = authentication.getPrincipal();

        if (user instanceof ServiceDefaultMethod serviceDefaultMethod) {
            service = serviceDefaultMethod;
        } else {
            System.out.println(user.getClass());
            throw new InstanceOfException("Principal is not instance of ServiceDefaultMethod");
        }

        authorities = service.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        return this.processGenerationToken(service.getId(), authorities, true, jsonWebTokenConnectionInformation, AuthenticationTypeConstants.SERVICE_JSON_WEB_TOKEN, AccountTypeConstants.SERVICE_DEFAULT_ACCOUNT, null);
    }

    @Override
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateSuperAdminWithDefaultMethod(SuperAdminAccountDTO account, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException {
        Object user;
        SuperAdminDefaultMethod superAdmin;
        String authorities;

        Authentication authentication = superAdminDefaultMethodAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword())
        );

        if (!authentication.isAuthenticated()){
            throw new BadCredentialsException("username or password incorrect");
        }

        user = authentication.getPrincipal();

        if (user instanceof SuperAdminDefaultMethod superAdminDefaultMethod){
            superAdmin = superAdminDefaultMethod;
        } else {
            throw new InstanceOfException("Principal is not instance of SuperAdminDefaultMethod");
        }

        authorities = superAdmin.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        // revoke all the token belong to the superAdmin to preserve the single session
        this.revokeAllSuperAdminCurrentConnection(superAdmin.getId());

        //generate the access and refresh token and save their jti and expirationDate in database
        return this.processGenerationToken(String.valueOf(superAdmin.getId()), authorities, true, jsonWebTokenConnectionInformation, AuthenticationTypeConstants.SUPER_ADMIN_JSON_WEB_TOKEN, AccountTypeConstants.SUPER_ADMIN_DEFAULT_ACCOUNT, null);

    }

    @Override
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateAdminWithDefaultMethod(AccountDTO account, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, AccountSuspendedException, ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        AdminDefaultMethod admin;
        Object user;
        String authorities;

        Authentication authentication = adminDefaultMthodAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword())
        );

        if (!authentication.isAuthenticated()){
            throw new BadCredentialsException("username or password incorrect");
        }

        user = authentication.getPrincipal();

        if (user instanceof AdminDefaultMethod adminDefaultMethod) {
            admin = adminDefaultMethod;
        } else
            throw new InstanceOfException("Principal is not instance of CandidateDefaultMethod");
        if (admin.isSuspended()){
            throw new AccountSuspendedException("account is suspended");
        }

        authorities = admin.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        // revoke all the token belong to the Admin to preserve the single session
        this.revokeAllAccountCurrentConnection(admin.getId());

        return this.processGenerationToken(String.valueOf(admin.getId()), authorities, true, jsonWebTokenConnectionInformation, AuthenticationTypeConstants.ACCOUNT_JSON_WEB_TOKEN, AccountTypeConstants.ADMIN_DEFAULT_ACCOUNT, UserTypeConstants.USER_ADMIN);
    }

    @Override
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateCandidateWithDefaultMethod(AccountDTO account, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws InstanceOfException, AccountActivationException, ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException {
        CandidateDefaultMethod candidate;
        Object user;
        String authorities;

        Authentication authentication = candidateDefaultMthodAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword())
        );

        if (!authentication.isAuthenticated()){
            throw new BadCredentialsException("username or password incorrect");
        }

        user = authentication.getPrincipal();

        if (user instanceof CandidateDefaultMethod candidateDefaultMethod) {
            candidate = candidateDefaultMethod;
        } else
            throw new InstanceOfException("Principal is not instance of CandidateDefaultMethod");
        if (!candidate.isActivated()){
            throw new AccountActivationException("account is not activated");
        }

        authorities = candidate.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        return this.processGenerationToken(String.valueOf(candidate.getId()), authorities, true, jsonWebTokenConnectionInformation, AuthenticationTypeConstants.ACCOUNT_JSON_WEB_TOKEN, AccountTypeConstants.CANDIDATE_DEFAULT_ACCOUNT, UserTypeConstants.USER_CANDIDATE);
    }

    @Override
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateCandidateWithGoogleMethod(String email, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws UsernameNotFoundException, InstanceOfException, ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException {
        CandidateGoogleMethod candidate;
        Object user;
        String authorities;
        Authentication authentication;

        authentication = candidateGoogleMethodAuthenticationManger.authenticate(
                new UsernamePasswordAuthenticationToken(email, "withoutPassword")
        );

        if (!authentication.isAuthenticated()){
            throw new BadCredentialsException("username or password incorrect");
        }

        user = authentication.getPrincipal();

        if (user instanceof CandidateGoogleMethod candidateGoogleMethod) {
            candidate = candidateGoogleMethod;
        } else {
            throw new InstanceOfException("Principal is not instance of CandidateDefaultMethod");
        }

        authorities = candidate.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        return this.processGenerationToken(String.valueOf(candidate.getId()), authorities, true, jsonWebTokenConnectionInformation, AuthenticationTypeConstants.ACCOUNT_JSON_WEB_TOKEN, AccountTypeConstants.CANDIDATE_GOOGLE_ACCOUNT, UserTypeConstants.USER_CANDIDATE);
    }

    @Override
    public AccessTokenAndRefreshTokenAndFingerPrintDTO authenticateWithRefreshToken(String refreshToken, String fingerprint, JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation) throws ValueNullException, InstanceOfException, UnprocessableAccountTypeException, UnknownAccountTypeException, AccountSuspendedException, AccountActivationException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        Jwt jwt;
        String accountType;
        String authorities;
        String hashedCookieFingerprint;

        if (refreshToken == null){
            throw new ValueNullException("refresh token is null");
        }

        if (fingerprint == null) {
            throw new CookieTokenFingerprintNotFoundException("fingerprint not found within request");
        }

        BearerTokenAuthenticationToken bearerTokenAuthenticationToken = new BearerTokenAuthenticationToken(refreshToken);
        JwtAuthenticationToken jwtAuthenticationToken;
        jwtAuthenticationToken = (JwtAuthenticationToken) authenticationManagerRefreshToken.authenticate(bearerTokenAuthenticationToken);

        if (!jwtAuthenticationToken.isAuthenticated()){
            throw new InvalidBearerTokenException("refresh token is invalid");
        }

        if (jwtAuthenticationToken.getToken() instanceof Jwt token){
            jwt = token;
        } else {
            throw new InstanceOfException("Principal is not instance of Jwt");
        }

        if (this.jwtIsRevoked(jwt.getId())){
            throw new InvalidBearerTokenException("refresh token is invalid");
        }

        hashedCookieFingerprint = hashUtil.hashWithSHA256AndReturnHexadecimalString(fingerprint.getBytes());
        if(!hashedCookieFingerprint.equals(jwt.getClaim(JwtClaimsConstants.FINGERPRINT))){
            throw new CookieFingerprintNotEqualWithJwtClaimException("The cookie value of fingerprint is not the same with value of jwt fingerprint");
        }

        accountType = jwt.getClaim(JwtClaimsConstants.ACCOUNT_TYPE);
        switch (accountType){
            case AccountTypeConstants.SUPER_ADMIN_DEFAULT_ACCOUNT: {
                SuperAdminDefaultMethod superAdmin;
                if (superAdminDefaultAccountRefreshTokenUserDetailsService.loadUserByUsername(jwt.getSubject()) instanceof SuperAdminDefaultMethod superAdminDefaultMethod){
                    superAdmin = superAdminDefaultMethod;
                } else {
                    throw new InstanceOfException("UserDetails is not instance of SuperAdminDefaultMethod");
                }

                authorities = superAdmin.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

                // revoke all the token belong to the superAdmin to preserve the single session
                for (SuperAdminJsonWebToken sAJwt : superAdminJsonWebTokenRepository.findAllByIdAccount(superAdmin.getId())) {
                    this.revokeJsonWebToken(new JsonWebTokenJtiAndDateExpirationDTO(sAJwt.getJti(), sAJwt.getExpirationDate()),
                            AuthenticationTypeConstants.SUPER_ADMIN_JSON_WEB_TOKEN);
                }

                //generate the access and refresh token and save their jti and expirationDate in database
                return this.processGenerationToken(String.valueOf(superAdmin.getId()), authorities, true, jsonWebTokenConnectionInformation, AuthenticationTypeConstants.SUPER_ADMIN_JSON_WEB_TOKEN, AccountTypeConstants.SUPER_ADMIN_DEFAULT_ACCOUNT, null);


            }
            case AccountTypeConstants.ADMIN_DEFAULT_ACCOUNT: {
                AdminDefaultMethod admin;
                if(adminDefaultAccountRefreshTokenUserDetailsService.loadUserByUsername(jwt.getSubject()) instanceof AdminDefaultMethod adminDefaultMethod){
                    admin = adminDefaultMethod;
                } else {
                    throw new InstanceOfException("UserDetails is not instance of AdminDefaultMethod");
                }

                if (admin.isSuspended()){
                    throw new AccountSuspendedException("account is suspended");
                }

                authorities = admin.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

                // revoke all the token belong to the Admin to preserve the single session
                for (AccountJsonWebToken aJwt : accountJsonWebTokenRepository.findAllByIdAccount(admin.getId())) {
                    this.revokeJsonWebToken(new JsonWebTokenJtiAndDateExpirationDTO(aJwt.getJti(), aJwt.getExpirationDate()),
                            AuthenticationTypeConstants.ACCOUNT_JSON_WEB_TOKEN);
                }

                return this.processGenerationToken(String.valueOf(admin.getId()), authorities, true, jsonWebTokenConnectionInformation, AuthenticationTypeConstants.ACCOUNT_JSON_WEB_TOKEN, AccountTypeConstants.ADMIN_DEFAULT_ACCOUNT, UserTypeConstants.USER_ADMIN);


            }
            case AccountTypeConstants.CANDIDATE_DEFAULT_ACCOUNT: {
                CandidateDefaultMethod candidate;
                if (candidateDefaultAccountRefreshTokenUserDetailsService.loadUserByUsername(jwt.getSubject()) instanceof CandidateDefaultMethod candidateDefaultMethod){
                    candidate = candidateDefaultMethod;
                } else {
                    throw new InstanceOfException("UserDetails is not instance of CandidateDefaultMethod");
                }

                if (!candidate.isActivated()){
                    throw new AccountActivationException("account is not activated");
                }

                authorities = candidate.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

                return this.processGenerationToken(String.valueOf(candidate.getId()), authorities, true, jsonWebTokenConnectionInformation, AuthenticationTypeConstants.ACCOUNT_JSON_WEB_TOKEN, AccountTypeConstants.CANDIDATE_DEFAULT_ACCOUNT, UserTypeConstants.USER_CANDIDATE);

            }
            case AccountTypeConstants.CANDIDATE_GOOGLE_ACCOUNT: {
                CandidateGoogleMethod candidate;
                if (candidateGoogleAccountRefreshTokenUserDetailsService.loadUserByUsername(jwt.getSubject()) instanceof CandidateGoogleMethod candidateGoogleMethod){
                    candidate = candidateGoogleMethod;
                } else {
                    throw new InstanceOfException("UserDetails is not instance of CandidateGoogleMethod");
                }

                authorities = candidate.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
                return this.processGenerationToken(String.valueOf(candidate.getId()), authorities, true, jsonWebTokenConnectionInformation, AuthenticationTypeConstants.ACCOUNT_JSON_WEB_TOKEN, AccountTypeConstants.CANDIDATE_GOOGLE_ACCOUNT, UserTypeConstants.USER_CANDIDATE);
            }
            case AccountTypeConstants.SERVICE_DEFAULT_ACCOUNT: {
                throw new UnprocessableAccountTypeException("Unprocessable Account Type");
            }
            default: throw new UnknownAccountTypeException("Unknown Account Type Exception");
        }
    }

    @Override
    public CandidateInformationWithoutIdAccountDTO obtainCandidateSGoogleAccountInformation(String googleAuthorizationCode) throws IOException, GeneralSecurityException, GoogleIdTokenNotValidException {
        CandidateInformationWithoutIdAccountDTO candidateInformation = new CandidateInformationWithoutIdAccountDTO();
        GoogleTokenResponse googleTokenResponse = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(),
                new GsonFactory(),
                googleOAuth2Credential.clientId(),
                googleOAuth2Credential.clientSecret(),
                googleAuthorizationCode,
                this.frontEndURL.url()
        ).execute();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singleton(googleOAuth2Credential.clientId())).build();
        GoogleIdToken idToken = verifier.verify(googleTokenResponse.getIdToken());
        System.out.println( "id token : " + idToken);
        if (idToken == null){
            throw new GoogleIdTokenNotValidException("id token not valid after the verification with Google signature");
        }

        candidateInformation.setEmail((String) idToken.getPayload().get("email"));
        candidateInformation.setFirstName((String) idToken.getPayload().get("given_name"));
        candidateInformation.setLastName((String) idToken.getPayload().get("family_name"));
        candidateInformation.setImageUrl((String) idToken.getPayload().get("picture"));
        candidateInformation.setGender((String) idToken.getPayload().get("gender"));

        return candidateInformation;
    }

    @Override
    public String obtainGoogleAuthenticationUrl() {
        String url =  new GoogleAuthorizationCodeRequestUrl(googleOAuth2Credential.clientId(),
                frontEndURL.url(),
                Arrays.asList("https://www.googleapis.com/auth/userinfo.email",
                        "https://www.googleapis.com/auth/userinfo.profile",
                        "https://www.googleapis.com/auth/user.gender.read")
        ).build();
        return url;
    }

//    @Override
    private void saveAccountJsonWebToken(JsonWebTokenJtiAndDateExpirationAndIdAccountAndConnectionInformationDTO jtiAndDateExpirationAndIdAccountAndConnectionInformation, String userType) throws ValueNullException {

        try {
            if (jtiAndDateExpirationAndIdAccountAndConnectionInformation.getJti() == null
                    || jtiAndDateExpirationAndIdAccountAndConnectionInformation.getDateExpiration() == null
                    || jtiAndDateExpirationAndIdAccountAndConnectionInformation.getIdAccount() == null
                    || Long.parseLong(jtiAndDateExpirationAndIdAccountAndConnectionInformation.getIdAccount()) == 0) {
                throw new ValueNullException("the jti value or dateExpiration value or idAccount are null or some of them");
            }
        } catch (NumberFormatException e){
            throw new ValueNullException("the jti value or dateExpiration value or idAccount are null or some of them");
        }

        AccountJsonWebToken accountJsonWebToken = new AccountJsonWebToken(
                jtiAndDateExpirationAndIdAccountAndConnectionInformation.getJti(),
                jtiAndDateExpirationAndIdAccountAndConnectionInformation.getDateExpiration(),
                Long.parseLong(jtiAndDateExpirationAndIdAccountAndConnectionInformation.getIdAccount()),
                jtiAndDateExpirationAndIdAccountAndConnectionInformation.getIp(),
                jtiAndDateExpirationAndIdAccountAndConnectionInformation.getIssuedAt(),
                jtiAndDateExpirationAndIdAccountAndConnectionInformation.getUserAgent(),
                userType);

        accountJsonWebTokenRepository.save(accountJsonWebToken);

    }

//    @Override
    private void saveServiceJsonWebToken(JsonWebTokenJtiAndDateExpirationAndIdServiceDTO jtiAndDateExpirationAndIdService) throws ValueNullException {
        if (jtiAndDateExpirationAndIdService.getJti() == null
                || jtiAndDateExpirationAndIdService.getDateExpiration() == null
                || jtiAndDateExpirationAndIdService.getIdService() == null){
            throw new ValueNullException("the jti value or dateExpiration value or idService are null or some of them");
        }

        ServiceJsonWebToken serviceJsonWebToken = new ServiceJsonWebToken(
                jtiAndDateExpirationAndIdService.getJti(),
                jtiAndDateExpirationAndIdService.getDateExpiration(),
                jtiAndDateExpirationAndIdService.getIp(),
                jtiAndDateExpirationAndIdService.getIssuedAt(),
                jtiAndDateExpirationAndIdService.getIdService());

        serviceJsonWebTokenRepository.save(serviceJsonWebToken);
    }

//    @Override
    private void saveSuperAdminJsonWebToken(JsonWebTokenJtiAndDateExpirationAndIdAccountAndConnectionInformationDTO jtiAndDateExpirationAndIdAccountAndConnectionInformation) throws ValueNullException {
        if (jtiAndDateExpirationAndIdAccountAndConnectionInformation.getJti() == null
                || jtiAndDateExpirationAndIdAccountAndConnectionInformation.getDateExpiration() == null
                || jtiAndDateExpirationAndIdAccountAndConnectionInformation.getIdAccount() == null){
            throw new ValueNullException("the jti value or dateExpiration value or idAccount are null or some of them");
        }

        SuperAdminJsonWebToken superAdminJsonWebToken = new SuperAdminJsonWebToken(
                jtiAndDateExpirationAndIdAccountAndConnectionInformation.getJti(),
                jtiAndDateExpirationAndIdAccountAndConnectionInformation.getDateExpiration(),
                jtiAndDateExpirationAndIdAccountAndConnectionInformation.getIp(),
                jtiAndDateExpirationAndIdAccountAndConnectionInformation.getIssuedAt(),
                jtiAndDateExpirationAndIdAccountAndConnectionInformation.getIdAccount(),
                jtiAndDateExpirationAndIdAccountAndConnectionInformation.getUserAgent());

        superAdminJsonWebTokenRepository.save(superAdminJsonWebToken);
    }

    @Override
    public boolean jwtIsRevoked(String jti) {
        return revokedJsonWebTokenRepository.findByJti(jti) != null;
    }

    @Override
    public void revokeAllAccountCurrentConnection(long idAccount) throws ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        for (AccountJsonWebToken jwt : accountJsonWebTokenRepository.findAllByIdAccount(idAccount)) {
            this.revokeJsonWebToken(new JsonWebTokenJtiAndDateExpirationDTO(jwt.getJti(), jwt.getExpirationDate()),
                    AuthenticationTypeConstants.ACCOUNT_JSON_WEB_TOKEN);
        }
    }

    @Override
    public void revokeAllSuperAdminCurrentConnection(String idAccount) throws ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        for (SuperAdminJsonWebToken jwt : superAdminJsonWebTokenRepository.findAllByIdAccount(idAccount)) {
            this.revokeJsonWebToken(new JsonWebTokenJtiAndDateExpirationDTO(jwt.getJti(), jwt.getExpirationDate()),
                    AuthenticationTypeConstants.SUPER_ADMIN_JSON_WEB_TOKEN);
        }
    }

    @Override
    public void revokeAllServiceCurrentConnection(String idService) throws ValueNullException, AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException {
        for (ServiceJsonWebToken jwt : serviceJsonWebTokenRepository.findAllByIdService(idService)) {
            this.revokeJsonWebToken(new JsonWebTokenJtiAndDateExpirationDTO(jwt.getJti(), jwt.getExpirationDate()),
                    AuthenticationTypeConstants.SERVICE_JSON_WEB_TOKEN);
        }
    }


    @Override
    public void removeExpiredJsonWebToken() {
        Instant currentInstant = Instant.now();
        revokedJsonWebTokenRepository.findAll();
        revokedJsonWebTokenRepository.deleteAllByExpirationDateIsBefore(currentInstant);
        serviceJsonWebTokenRepository.deleteAllByExpirationDateIsBefore(currentInstant);
        superAdminJsonWebTokenRepository.deleteAllByExpirationDateIsBefore(currentInstant);
        accountJsonWebTokenRepository.deleteAllByExpirationDateIsBefore(currentInstant);
    }

    @Override
    public Map<Long, AuthenticationInformationDTO> getAllAccountAuthentications() {
        List<AccountAuthentication> accountAuthenticationList;
        Map<Long, AuthenticationInformationDTO> authenticationAccountDTOMap;

        accountAuthenticationList = accountAuthenticationRepository.findAll();
        if (accountAuthenticationList.isEmpty()){
            return new HashMap<>();
        }

        authenticationAccountDTOMap = new HashMap<>();

        for (AccountAuthentication authentication : accountAuthenticationList){
            authenticationAccountDTOMap.put(authentication.getIdAccount(),
                    new AuthenticationInformationDTO(authentication.getIdAccount(),
                            authentication.getLastIpConnection(),
                            authentication.getLastConnection(),
                            authentication.getUserAgent(),
                            authentication.getScreenResolution(),
                            authentication.getUserType()));
        }

        return authenticationAccountDTOMap;
    }

    @Override
    public Map<Long, AuthenticationInformationDTO> getAllAdminAuthentications() {
        List<AccountAuthentication> accountAuthenticationList;
        Map<Long, AuthenticationInformationDTO> authenticationAccountDTOMap;

        accountAuthenticationList = accountAuthenticationRepository.findAllByUserType(UserTypeConstants.USER_ADMIN);
        if (accountAuthenticationList.isEmpty()){
            return new HashMap<>();
        }

        authenticationAccountDTOMap = new HashMap<>();

        for (AccountAuthentication authentication : accountAuthenticationList){
            authenticationAccountDTOMap.put(authentication.getIdAccount(),
                    new AuthenticationInformationDTO(authentication.getIdAccount(),
                            authentication.getLastIpConnection(),
                            authentication.getLastConnection(),
                            authentication.getUserAgent(),
                            authentication.getScreenResolution(),
                            authentication.getUserType()));
        }

        return authenticationAccountDTOMap;
    }

    @Override
    public Map<Long, AuthenticationInformationDTO> getAllCandidateAuthentications() {
        List<AccountAuthentication> accountAuthenticationList;
        Map<Long, AuthenticationInformationDTO> authenticationAccountDTOMap;

        accountAuthenticationList = accountAuthenticationRepository.findAllByUserType(UserTypeConstants.USER_CANDIDATE);
        if (accountAuthenticationList.isEmpty()){
            return new HashMap<>();
        }

        authenticationAccountDTOMap = new HashMap<>();

        for (AccountAuthentication authentication : accountAuthenticationList){
            authenticationAccountDTOMap.put(authentication.getIdAccount(),
                    new AuthenticationInformationDTO(authentication.getIdAccount(),
                            authentication.getLastIpConnection(),
                            authentication.getLastConnection(),
                            authentication.getUserAgent(),
                            authentication.getScreenResolution(),
                            authentication.getUserType()));
        }

        return authenticationAccountDTOMap;
    }

    @Override
    public AuthenticationInformationDTO getOneAccountAuthentications(long idAccount) {
        Optional<AccountAuthentication> optional = accountAuthenticationRepository.findById(idAccount);
        if (optional.isEmpty()){
            return null;
        }

        AccountAuthentication accountAuthentication = optional.get();
        return new AuthenticationInformationDTO(
                accountAuthentication.getIdAccount(),
                accountAuthentication.getLastIpConnection(),
                accountAuthentication.getLastConnection(),
                accountAuthentication.getUserAgent(),
                accountAuthentication.getScreenResolution(),
                accountAuthentication.getUserType());
    }

    @Override
    public SuperAdminAuthenticationInformationDTO getSuperAdminAuthentication() {
        Optional<SuperAdminAuthentication> optional = superAdminAuthenticationRepository.findById(SuperAdminIdAccountConstants.SUPER_ADMIN_ID_ACCOUNT);
        if (optional.isEmpty()){
            return null;
        }

        SuperAdminAuthentication superAdminAuthentication = optional.get();
        return new SuperAdminAuthenticationInformationDTO(
                superAdminAuthentication.getLastIpConnection(),
                superAdminAuthentication.getLastConnection(),
                superAdminAuthentication.getUserAgent(),
                superAdminAuthentication.getScreenResolution());
    }

    @Override
    public Map<Long, List<AccountConnectionInformationDTO>> getAllAccountCurrentConnections() {
        Map<Long, List<AccountConnectionInformationDTO>> listMap = new HashMap<>();
        AccountConnectionInformationDTO accountConnectionInformationDTO = null;
        List<AccountJsonWebToken> accountJsonWebTokens = accountJsonWebTokenRepository.findAll();

        for (AccountJsonWebToken accountJsonWebToken : accountJsonWebTokens){
            accountConnectionInformationDTO = new AccountConnectionInformationDTO(
                    accountJsonWebToken.getIdAccount(),
                    accountJsonWebToken.getJti(),
                    LocalDateTime.ofInstant(accountJsonWebToken.getExpirationDate(),ZoneId.systemDefault()),
                    accountJsonWebToken.getIp(),
                    LocalDateTime.ofInstant(accountJsonWebToken.getIssuedAt(), ZoneId.systemDefault()),
                    accountJsonWebToken.getUserAgent());
            if(!listMap.containsKey(accountJsonWebToken.getIdAccount())){
                listMap.put(accountJsonWebToken.getIdAccount(),
                        new ArrayList<AccountConnectionInformationDTO>(List.of(accountConnectionInformationDTO)));
            } else {
                listMap.get(accountJsonWebToken.getIdAccount()).add(accountConnectionInformationDTO);
            }
            accountConnectionInformationDTO = null;
        }

        return listMap;
    }

    @Override
    public Map<String, List<SuperAdminConnectionInformationDTO>> getAllSuperAdminCurrentConnections() {
        Map<String, List<SuperAdminConnectionInformationDTO>> listMap = new HashMap<>();
        SuperAdminConnectionInformationDTO superAdminConnectionInformationDTO = null;
        List<SuperAdminJsonWebToken> superAdminJsonWebTokens = superAdminJsonWebTokenRepository.findAll();

        for (SuperAdminJsonWebToken superAdminJsonWebToken : superAdminJsonWebTokens){
            superAdminConnectionInformationDTO = new SuperAdminConnectionInformationDTO(
                    superAdminJsonWebToken.getIdAccount(),
                    superAdminJsonWebToken.getJti(),
                    LocalDateTime.ofInstant(superAdminJsonWebToken.getExpirationDate(),ZoneId.systemDefault()),
                    superAdminJsonWebToken.getIp(),
                    LocalDateTime.ofInstant(superAdminJsonWebToken.getIssuedAt(), ZoneId.systemDefault()),
                    superAdminJsonWebToken.getUserAgent());
            if(!listMap.containsKey(superAdminJsonWebToken.getIdAccount())){
                listMap.put(superAdminJsonWebToken.getIdAccount(),
                        new ArrayList<SuperAdminConnectionInformationDTO>(List.of(superAdminConnectionInformationDTO)));
            } else {
                listMap.get(superAdminJsonWebToken.getIdAccount()).add(superAdminConnectionInformationDTO);
            }
            superAdminConnectionInformationDTO = null;
        }

        return listMap;
    }

    @Override
    public Map<String, List<ServiceConnectionInformationDTO>> getAllServiceCurrentConnections() {
        Map<String, List<ServiceConnectionInformationDTO>> listMap = new HashMap<>();
        ServiceConnectionInformationDTO serviceConnectionInformationDTO = null;
        List<ServiceJsonWebToken> serviceJsonWebTokens = serviceJsonWebTokenRepository.findAll();

        for (ServiceJsonWebToken serviceJsonWebToken : serviceJsonWebTokens){
            serviceConnectionInformationDTO = new ServiceConnectionInformationDTO(
                    serviceJsonWebToken.getIdService(),
                    serviceJsonWebToken.getJti(),
                    LocalDateTime.ofInstant(serviceJsonWebToken.getExpirationDate(),ZoneId.systemDefault()),
                    serviceJsonWebToken.getIp(),
                    LocalDateTime.ofInstant(serviceJsonWebToken.getIssuedAt(), ZoneId.systemDefault()));
            if(!listMap.containsKey(serviceJsonWebToken.getIdService())){
                listMap.put(serviceJsonWebToken.getIdService(),
                        new ArrayList<ServiceConnectionInformationDTO>(List.of(serviceConnectionInformationDTO)));
            } else {
                listMap.get(serviceJsonWebToken.getIdService()).add(serviceConnectionInformationDTO);
            }
            serviceConnectionInformationDTO = null;
        }

        return listMap;
    }

    @Override
    public Map<Long, List<AccountConnectionInformationDTO>> getAllAccountCurrentConnectionsByUserType(String userType) {
        Map<Long, List<AccountConnectionInformationDTO>> listMap = new HashMap<>();
        AccountConnectionInformationDTO accountConnectionInformationDTO = null;
        List<AccountJsonWebToken> accountJsonWebTokens = accountJsonWebTokenRepository.findAllByUserType(userType);

        if (accountJsonWebTokens == null || accountJsonWebTokens.isEmpty()){
            return new HashMap<>();
        }

        for (AccountJsonWebToken accountJsonWebToken : accountJsonWebTokens){
            accountConnectionInformationDTO = new AccountConnectionInformationDTO(
                    accountJsonWebToken.getIdAccount(),
                    accountJsonWebToken.getJti(),
                    LocalDateTime.ofInstant(accountJsonWebToken.getExpirationDate(),ZoneId.systemDefault()),
                    accountJsonWebToken.getIp(),
                    LocalDateTime.ofInstant(accountJsonWebToken.getIssuedAt(), ZoneId.systemDefault()),
                    accountJsonWebToken.getUserAgent());
            if(!listMap.containsKey(accountJsonWebToken.getIdAccount())){
                listMap.put(accountJsonWebToken.getIdAccount(),
                        new ArrayList<AccountConnectionInformationDTO>(List.of(accountConnectionInformationDTO)));
            } else {
                listMap.get(accountJsonWebToken.getIdAccount()).add(accountConnectionInformationDTO);
            }
            accountConnectionInformationDTO = null;
        }

        return listMap;
    }

    @Override
    public List<AccountConnectionInformationDTO> getOneAccountCurrentConnections(long idAccount) {
        List<AccountConnectionInformationDTO> accountConnectionInformationDTOList = new ArrayList<>();
        List<AccountJsonWebToken> accountJsonWebTokens = accountJsonWebTokenRepository.findAllByIdAccount(idAccount);

        if (accountJsonWebTokens == null || accountJsonWebTokens.isEmpty()){
            return accountConnectionInformationDTOList;
        }

        for (AccountJsonWebToken accountJsonWebToken : accountJsonWebTokens){
            accountConnectionInformationDTOList.add(new AccountConnectionInformationDTO(
                    accountJsonWebToken.getIdAccount(),
                    accountJsonWebToken.getJti(),
                    LocalDateTime.ofInstant(accountJsonWebToken.getExpirationDate(),ZoneId.systemDefault()),
                    accountJsonWebToken.getIp(),
                    LocalDateTime.ofInstant(accountJsonWebToken.getIssuedAt(), ZoneId.systemDefault()),
                    accountJsonWebToken.getUserAgent()));

        }

        return accountConnectionInformationDTOList;
    }

    @Override
    public List<ServiceConnectionInformationDTO> getOneServiceCurrentConnections(String idService) {
        List<ServiceConnectionInformationDTO> serviceConnectionInformationList = new ArrayList<>();
        List<ServiceJsonWebToken> serviceJsonWebTokens = serviceJsonWebTokenRepository.findAllByIdService(idService);

        if (serviceJsonWebTokens == null || serviceJsonWebTokens.isEmpty()){
            return serviceConnectionInformationList;
        }

        for (ServiceJsonWebToken serviceJsonWebToken : serviceJsonWebTokens){
            serviceConnectionInformationList.add(new ServiceConnectionInformationDTO(
                    serviceJsonWebToken.getIdService(),
                    serviceJsonWebToken.getJti(),
                    LocalDateTime.ofInstant(serviceJsonWebToken.getExpirationDate(),ZoneId.systemDefault()),
                    serviceJsonWebToken.getIp(),
                    LocalDateTime.ofInstant(serviceJsonWebToken.getIssuedAt(), ZoneId.systemDefault())));
        }

        return serviceConnectionInformationList;
    }

    /*@Override
    public List<AccountConnectionInformationDTO> getAllMyConnections(long idAccount) throws ConnectionNotFoundException {
        List<AccountConnectionInformationDTO> accountConnectionInformationDTOS = new ArrayList<>();
        List<AccountJsonWebToken> myConnections =  accountJsonWebTokenRepository.findAllByIdAccountAndExpirationDateAfter(idAccount, Instant.now());

        if (myConnections == null
                || myConnections.isEmpty()){
            throw new ConnectionNotFoundException("there is a request for all connections associate with user and there is no connection. How did you request your connections and and you don't have at least one connection oO");
        }

        for (AccountJsonWebToken connection : myConnections){
            accountConnectionInformationDTOS.add(new AccountConnectionInformationDTO(connection.getJti(), LocalDateTime.ofInstant(connection.getIssuedAt(), ZoneId.systemDefault()), connection.getUserAgent(), connection.getIp()));
        }

        return accountConnectionInformationDTOS;

    }*/

    /*@Override
    public void revokeMyOtherJwt(long idAccount, String jti) {

    }*/

    /*@Override
    public List<ConnectionDTO> getAllConnections() {
        return null;
    }*/

    /*@Override
    public void revokeOtherJwt(String[] jtiList) {

    }*/

    /*@Override
    public ConnectionDTO getAccountConnections(long idAccount) {
        return null;
    }*/

    @Override
    public AccessTokenDTO generateAccessToken(JwtScopeAndSubjectAndFingerprintDTO jwtScopeAndSubjectAndFingerprint,
                                              JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation,
                                              String authenticationType,
                                              String userType) throws AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException, ValueNullException {

        if (authenticationType == null){
            throw new AuthenticationTypeNullPointerException("null pointer");
        }

        if (!AuthenticationTypeConstants.allTypes.contains(authenticationType)){
            throw new UnknownAuthenticationTypeException("there is an unknown type");
        }

        String accessToken;
        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(jwtClaim.issuer())
                .expiresAt(instant.plus(jwtClaim.accessTokenExpirationTime(),ChronoUnit.HOURS))
                .issuedAt(instant)
                .audience(List.of(jwtClaim.audience()))
                .subject(jwtScopeAndSubjectAndFingerprint.getSubject())
                .id(generateUniqueIdUtil.generateUniqueId())
                .claim(JwtClaimsConstants.Client_ID, jwtClaim.clientId())
                .claim(JwtClaimsConstants.SCOPE, jwtScopeAndSubjectAndFingerprint.getScope())
                .claim(JwtClaimsConstants.FINGERPRINT, hashUtil.hashWithSHA256AndReturnHexadecimalString(jwtScopeAndSubjectAndFingerprint.getFingerprint().getBytes()))
                .build();

        accessToken = accessJwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

        switch (authenticationType){
            case AuthenticationTypeConstants.WITHOUT_JSON_WEB_TOKEN: break;
            case AuthenticationTypeConstants.ACCOUNT_JSON_WEB_TOKEN:{
                this.saveAccountJsonWebToken(new JsonWebTokenJtiAndDateExpirationAndIdAccountAndConnectionInformationDTO(
                        jwtClaimsSet.getId(),
                        jwtClaimsSet.getExpiresAt(),
                        jwtClaimsSet.getSubject(),
                        jsonWebTokenConnectionInformation.getIp(),
                        instant,
                        jsonWebTokenConnectionInformation.getUserAgent()),
                        userType);

                AccountAuthentication accountAuthentication = new AccountAuthentication(
                        Long.parseLong(jwtClaimsSet.getSubject()),
                        jsonWebTokenConnectionInformation.getIp(),
                        LocalDateTime.now(),
                        jsonWebTokenConnectionInformation.getUserAgent(),
                        jsonWebTokenConnectionInformation.getScreenResolution(),
                        userType
                );

                accountAuthenticationRepository.save(accountAuthentication);

            } break;
            case AuthenticationTypeConstants.SUPER_ADMIN_JSON_WEB_TOKEN:{
                this.saveSuperAdminJsonWebToken(new JsonWebTokenJtiAndDateExpirationAndIdAccountAndConnectionInformationDTO(
                        jwtClaimsSet.getId(),
                        jwtClaimsSet.getExpiresAt(),
                        jwtClaimsSet.getSubject(),
                        jsonWebTokenConnectionInformation.getIp(),
                        instant,
                        jsonWebTokenConnectionInformation.getUserAgent()));

                SuperAdminAuthentication superAdminAuthentication = new SuperAdminAuthentication(
                        jwtClaimsSet.getSubject(),
                        jsonWebTokenConnectionInformation.getIp(),
                        LocalDateTime.now(),
                        jsonWebTokenConnectionInformation.getUserAgent(),
                        jsonWebTokenConnectionInformation.getScreenResolution()
                );

                superAdminAuthenticationRepository.save(superAdminAuthentication);

            } break ;
            case AuthenticationTypeConstants.SERVICE_JSON_WEB_TOKEN:{
                this.saveServiceJsonWebToken(new JsonWebTokenJtiAndDateExpirationAndIdServiceDTO(
                        jwtClaimsSet.getId(),
                        jwtClaimsSet.getExpiresAt(),
                        jwtClaimsSet.getSubject(),
                        jsonWebTokenConnectionInformation.getIp(),
                        instant));
            } break;
            default: throw new UnknownAuthenticationTypeException("there is an unknown type");
        }

        return new AccessTokenDTO(accessToken);
    }

    @Override
    public RefreshTokenDTO generateRefreshToken(JwtSubjectAndFingerprintDTO jwtSubjectAndFingerprint,
                                                JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation,
                                                String authenticationType,
                                                String accountType,
                                                String userType) throws AuthenticationTypeNullPointerException,
            UnknownAuthenticationTypeException,
            ValueNullException {

        if (authenticationType == null){
            throw new AuthenticationTypeNullPointerException("null pointer");
        }

        if (!AuthenticationTypeConstants.allTypes.contains(authenticationType)){
            throw new UnknownAuthenticationTypeException("there is an unknown type");
        }

        String refreshToken;
        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(jwtClaim.issuer())
                .expiresAt(instant.plus(jwtClaim.refreshTokenExpirationTime(),ChronoUnit.HOURS))
                .issuedAt(instant)
                .audience(List.of(jwtClaim.audience()))
                .subject(jwtSubjectAndFingerprint.getSubject())
                .id(generateUniqueIdUtil.generateUniqueId())
                .claim(JwtClaimsConstants.Client_ID, jwtClaim.clientId())
                .claim(JwtClaimsConstants.FINGERPRINT, hashUtil.hashWithSHA256AndReturnHexadecimalString(jwtSubjectAndFingerprint.getFingerprint().getBytes()))
                .claim(JwtClaimsConstants.ACCOUNT_TYPE, accountType)
                .build();

        refreshToken = refreshJwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

        switch (authenticationType){
            case AuthenticationTypeConstants.WITHOUT_JSON_WEB_TOKEN:
                break;
            case AuthenticationTypeConstants.ACCOUNT_JSON_WEB_TOKEN:{
                this.saveAccountJsonWebToken(new JsonWebTokenJtiAndDateExpirationAndIdAccountAndConnectionInformationDTO(
                        jwtClaimsSet.getId(),
                        jwtClaimsSet.getExpiresAt(),
                        jwtClaimsSet.getSubject(),
                        jsonWebTokenConnectionInformation.getIp(),
                        instant,
                        jsonWebTokenConnectionInformation.getUserAgent()),
                        userType);
            } break;
            case AuthenticationTypeConstants.SUPER_ADMIN_JSON_WEB_TOKEN:{
                this.saveSuperAdminJsonWebToken(new JsonWebTokenJtiAndDateExpirationAndIdAccountAndConnectionInformationDTO(
                        jwtClaimsSet.getId(),
                        jwtClaimsSet.getExpiresAt(),
                        jwtClaimsSet.getSubject(),
                        jsonWebTokenConnectionInformation.getIp(),
                        instant,
                        jsonWebTokenConnectionInformation.getUserAgent()));
            } break ;
            case AuthenticationTypeConstants.SERVICE_JSON_WEB_TOKEN:{
                this.saveServiceJsonWebToken(new JsonWebTokenJtiAndDateExpirationAndIdServiceDTO(
                        jwtClaimsSet.getId(),
                        jwtClaimsSet.getExpiresAt(),
                        jwtClaimsSet.getSubject(),
                        jsonWebTokenConnectionInformation.getIp(),
                        instant));
            } break;
            default: throw new UnknownAuthenticationTypeException("there is an unknown type");
        }

        return new RefreshTokenDTO(refreshToken);
    }

    @Override
    public String generateFingerprint(){
        return randomTokenGeneratorUtil.generateRandomToken32Characters();
    }

    private AccessTokenAndRefreshTokenAndFingerPrintDTO processGenerationToken(String subject, String authorities,
                                                                               boolean generateRefreshToken,
                                                                               JsonWebTokenConnectionInformationDTO jsonWebTokenConnectionInformation,
                                                                               String authenticationType,
                                                                               String accountType,
                                                                               String userType) throws ValueNullException, UnknownAuthenticationTypeException, AuthenticationTypeNullPointerException {
        AccessTokenAndRefreshTokenAndFingerPrintDTO jwtTokens = new AccessTokenAndRefreshTokenAndFingerPrintDTO(null, null, null);
        JwtScopeAndSubjectAndFingerprintDTO jwtScopeAndSubjectAndFingerprint = new JwtScopeAndSubjectAndFingerprintDTO();
        String fingerPrint;

        fingerPrint = this.generateFingerprint();

        jwtScopeAndSubjectAndFingerprint.setSubject(subject);
        jwtScopeAndSubjectAndFingerprint.setScope(authorities);
        jwtScopeAndSubjectAndFingerprint.setFingerprint(fingerPrint);

        if (generateRefreshToken){
            JwtSubjectAndFingerprintDTO jwtSubjectAndFingerprint = new JwtSubjectAndFingerprintDTO();
            jwtSubjectAndFingerprint.setSubject(subject);
            jwtSubjectAndFingerprint.setFingerprint(fingerPrint);

            jwtTokens.setRefreshToken(this.generateRefreshToken(jwtSubjectAndFingerprint, jsonWebTokenConnectionInformation, authenticationType, accountType, userType)
                    .getRefreshToken());
        }

        jwtTokens.setAccessToken(this.generateAccessToken(jwtScopeAndSubjectAndFingerprint, jsonWebTokenConnectionInformation, authenticationType, userType)
                .getAccessToken());
        jwtTokens.setFingerPrint(fingerPrint);

        return jwtTokens;
    }

    private void revokeJsonWebToken(JsonWebTokenJtiAndDateExpirationDTO jtiAndDateExpiration, String authenticationType) throws AuthenticationTypeNullPointerException, UnknownAuthenticationTypeException, ValueNullException {

        if (authenticationType == null){
            throw new AuthenticationTypeNullPointerException("null pointer");
        }

        if (!AuthenticationTypeConstants.allTypes.contains(authenticationType)){
            throw new UnknownAuthenticationTypeException("there is an unknown type");
        }

        if(jtiAndDateExpiration.getJti() == null || jtiAndDateExpiration.getJti().isBlank() || jtiAndDateExpiration.getJti().isEmpty()
                || jtiAndDateExpiration.getDateExpiration() == null){
            throw new ValueNullException("the value of jti or dateExpiration is empty or blank or null");
        }

        RevokedJsonWebToken revokedJsonWebToken = new RevokedJsonWebToken(jtiAndDateExpiration.getJti(), jtiAndDateExpiration.getDateExpiration());
        revokedJsonWebTokenRepository.save(revokedJsonWebToken);

        switch (authenticationType){
            case AuthenticationTypeConstants.WITHOUT_JSON_WEB_TOKEN:
                break;
            case AuthenticationTypeConstants.ACCOUNT_JSON_WEB_TOKEN:
                accountJsonWebTokenRepository.deleteById(jtiAndDateExpiration.getJti());
                break;
            case AuthenticationTypeConstants.SUPER_ADMIN_JSON_WEB_TOKEN:
                superAdminJsonWebTokenRepository.deleteById(jtiAndDateExpiration.getJti());
                break ;
            case AuthenticationTypeConstants.SERVICE_JSON_WEB_TOKEN:
                serviceJsonWebTokenRepository.deleteById(jtiAndDateExpiration.getJti());
                break;
            default: throw new UnknownAuthenticationTypeException("there is an unknown authentication type");
        }
    }
}
