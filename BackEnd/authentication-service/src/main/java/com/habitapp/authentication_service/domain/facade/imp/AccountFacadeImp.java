package com.habitapp.authentication_service.domain.facade.imp;

import com.habitapp.authentication_service.annotation.Facade;
import com.habitapp.authentication_service.domain.exception.account.*;
import com.habitapp.authentication_service.domain.facade.AccountFacade;
import com.habitapp.authentication_service.domain.service.AccountService;
import com.habitapp.authentication_service.domain.service.AuthenticationService;
import com.habitapp.authentication_service.dto.account.AccountAndInformationDTO;
import com.habitapp.authentication_service.dto.account.AccountEmailAndActivationTokenDTO;
import com.habitapp.authentication_service.dto.account.AccountIdAndAuthoritiesDTO;
import com.habitapp.authentication_service.dto.account.AccountIdAndEmailAndActivationURLDTO;
import com.habitapp.authentication_service.dto.email.EmailAndUrlDTO;
import com.habitapp.authentication_service.dto.jwt.AccessTokenAndRefreshTokenDTO;
import com.habitapp.authentication_service.dto.jwt.JwtScopeAndSubjectDTO;
import com.habitapp.authentication_service.dto.jwt.JwtSubjectDTO;
import com.habitapp.authentication_service.proxy.client.emailing.EmailServiceProxy;
import com.habitapp.authentication_service.proxy.client.profile.IndividualServiceProxy;
import com.habitapp.authentication_service.proxy.exception.common.*;
import com.habitapp.common.http.request_response.individual.IndividualRequestResponseHttp;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Facade
public class AccountFacadeImp implements AccountFacade {
    private final AuthenticationService authenticationService;
    private AccountService accountService;
    private IndividualServiceProxy individualServiceProxy;
    private EmailServiceProxy emailServiceProxy;

    public void createIndividualAccountWithDefaultMethod(AccountAndInformationDTO account) throws EmailPatternNotValidException, PasswordPatternNotValidException, EmailNotFoundException, PasswordNotFoundException, UrlConfigurationNotFoundException, AccountAlreadyExistsException, RoleNotFoundException, RolePrefixException, RoleNotDefinedException, AccountNotCreatedException, PermissionPrefixException, PermissionNotDefinedException, UnexpectedException, UnauthorizedException, UnprocessableEntityException, ForbiddenException, InternalServerErrorException {
        AccountIdAndEmailAndActivationURLDTO accountIdAndEmailAndActivationURLDTO = accountService.createIndividualAccountWithDefaultMethod(account);
        individualServiceProxy.createIndividual(new IndividualRequestResponseHttp(accountIdAndEmailAndActivationURLDTO.getIdAccount(), account.getFirstName(), account.getLastName(), account.getEmail(), account.getGender(), account.getBirthDate(), account.getPassword()));
        emailServiceProxy.sendURLActivationAccount(new EmailAndUrlDTO(accountIdAndEmailAndActivationURLDTO.getEmail(), accountIdAndEmailAndActivationURLDTO.getActivationURL()));
    }
    @Override
    public AccessTokenAndRefreshTokenDTO activateTheIndividualAccountCreatedByDefaultMethod(AccountEmailAndActivationTokenDTO accountEmailAndActivationTokenDTO) throws EmailNotFoundException, VerificationTokenNotFoundException, EmailPatternNotValidException, VerificationTokenPatternNotValidException, AccountNotFoundException, VerificationTokenDurationExpiredException, AccountIsActivatedException, VerificationTokensNotEqualsException {
        AccountIdAndAuthoritiesDTO accountIdAndAuthorities = accountService.activateTheIndividualAccountCreatedByDefaultMethod(accountEmailAndActivationTokenDTO);

        JwtScopeAndSubjectDTO jwtScopeAndSubject =  new JwtScopeAndSubjectDTO();
        jwtScopeAndSubject.setSubject(accountIdAndAuthorities.getIdAccount());
        jwtScopeAndSubject.setScope(accountIdAndAuthorities.getAuthorities());

        JwtSubjectDTO jwtSubject = new JwtSubjectDTO();
        jwtSubject.setSubject(accountIdAndAuthorities.getIdAccount());

        AccessTokenAndRefreshTokenDTO accessTokenAndRefreshToken = new AccessTokenAndRefreshTokenDTO();
        accessTokenAndRefreshToken.setRefreshToken(authenticationService
                .generateRefreshToken(jwtSubject)
                .getRefreshToken());
        accessTokenAndRefreshToken.setAccessToken(authenticationService
                .generateAccessToken(jwtScopeAndSubject)
                .getAccessToken());

        return accessTokenAndRefreshToken;
    }



    @Override
    public void updateIndividualAccountWithDefaultMethod(AccountAndInformationDTO account) throws PasswordPatternNotValidException, PasswordNotFoundException, AccountNotFoundException, UnexpectedException, UnauthorizedException {
        accountService.updatePasswordIndividualAccountWithDefaultMethod(account);
        individualServiceProxy.updateIndividual(account.getIdAccount(), new IndividualRequestResponseHttp(account.getIdAccount(), account.getFirstName(), account.getLastName(), account.getEmail(), account.getGender(), account.getBirthDate(), ""));
    }
}
