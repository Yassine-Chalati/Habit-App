package com.habitapp.authentication_service.domain.facade.imp;

import com.habitapp.authentication_service.domain.exception.account.*;
import com.habitapp.authentication_service.domain.exception.account.RoleNotDefinedException;
import com.habitapp.authentication_service.domain.exception.account.RoleNotFoundException;
import com.habitapp.authentication_service.domain.exception.account.RolePrefixException;
import com.habitapp.authentication_service.domain.facade.AccountFacade;
import com.habitapp.authentication_service.annotation.Facade;
import com.habitapp.authentication_service.domain.service.AccountService;
import com.habitapp.authentication_service.dto.account.*;
import com.habitapp.authentication_service.dto.email.EmailAndUrlDTO;
import com.habitapp.authentication_service.proxy.client.emailing.EmailServiceProxy;
import com.habitapp.authentication_service.proxy.client.profile.IndividualServiceProxy;
import com.habitapp.authentication_service.proxy.exception.common.*;
import com.habitapp.profile_service.domain.entity.Individual;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Facade
public class AccountFacadeImp implements AccountFacade {
    private AccountService accountService;
    private IndividualServiceProxy individualServiceProxy;
    private EmailServiceProxy emailServiceProxy;

    public void createIndividualAccountWithDefaultMethod(AccountAndInformationDTO account) throws EmailPatternNotValidException, PasswordPatternNotValidException, EmailNotFoundException, PasswordNotFoundException, UrlConfigurationNotFoundException, AccountAlreadyExistsException, RoleNotFoundException, RolePrefixException, RoleNotDefinedException, AccountNotCreatedException, PermissionPrefixException, PermissionNotDefinedException, UnexpectedException, UnauthorizedException, UnprocessableEntityException, ForbiddenException, InternalServerErrorException {
        AccountIdAndEmailAndActivationURLDTO accountIdAndEmailAndActivationURLDTO = accountService.createIndividualAccountWithDefaultMethod(account);
        individualServiceProxy.createIndividual(new Individual(0, account.getFirstName(), account.getLastName(), account.getEmail(), account.getGender(), account.getBirthDate()));
        emailServiceProxy.sendURLActivationAccount(new EmailAndUrlDTO(accountIdAndEmailAndActivationURLDTO.getEmail(), accountIdAndEmailAndActivationURLDTO.getActivationURL()));
    }
    @Override
    public AccountIdAndAuthoritiesDTO activateTheIndividualAccountCreatedByDefaultMethod(AccountEmailAndActivationTokenDTO accountEmailAndActivationTokenDTO) throws EmailNotFoundException, VerificationTokenNotFoundException, EmailPatternNotValidException, VerificationTokenPatternNotValidException, AccountNotFoundException, VerificationTokenDurationExpiredException, AccountIsActivatedException, VerificationTokensNotEqualsException {
        return accountService.activateTheIndividualAccountCreatedByDefaultMethod(accountEmailAndActivationTokenDTO);
    }



    @Override
    public void updateIndividualAccountWithDefaultMethod(AccountAndInformationDTO account) throws PasswordPatternNotValidException, PasswordNotFoundException, AccountNotFoundException, UnexpectedException, UnauthorizedException {
        accountService.updatePasswordIndividualAccountWithDefaultMethod(account);
        individualServiceProxy.updateIndividual(account.getIdAccount(), new Individual(account.getIdAccount(), account.getFirstName(), account.getLastName(), account.getEmail(), account.getGender(), account.getBirthDate()));
    }
}
