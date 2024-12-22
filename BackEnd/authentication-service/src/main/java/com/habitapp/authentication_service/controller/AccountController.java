package com.habitapp.authentication_service.controller;

import com.habitapp.authentication_service.common.constant.AccountHttpResponseConstants;
import com.habitapp.authentication_service.common.handler.time.TimeHandler;
import com.habitapp.authentication_service.configuration.record.HttpResponseTime;
import com.habitapp.authentication_service.configuration.record.JwtClaim;
import com.habitapp.authentication_service.domain.exception.account.*;
import com.habitapp.authentication_service.domain.facade.AccountFacade;
import com.habitapp.authentication_service.domain.facade.AuthenticationFacade;
import com.habitapp.authentication_service.domain.facade.EmailFacade;
import com.habitapp.authentication_service.dto.account.AccountAndInformationDTO;
import com.habitapp.authentication_service.dto.account.AccountEmailAndActivationTokenDTO;
import com.habitapp.authentication_service.dto.jwt.AccessTokenAndRefreshTokenDTO;
import com.habitapp.authentication_service.proxy.exception.common.*;
import com.habitapp.common.common.account.RoleNameCommonConstants;
import com.habitapp.common.http.request_response.individual.IndividualRequestResponseHttp;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {
    private AccountFacade accountFacade;
    private EmailFacade emailFacade;
    private AuthenticationFacade authenticationFacade;
    private JwtClaim jwtClaim;
    private HttpResponseTime responseTime;

    @PostMapping("/individual/default-method/create")
    public ResponseEntity<String> createIndividualAccountWithDefaultMethod(@RequestBody IndividualRequestResponseHttp accountRequestHttp){
        TimeHandler timeHandler = new TimeHandler(responseTime.responseTimeWithDispatching());
        timeHandler.start();
        HttpHeaders httpHeaders = new HttpHeaders();
        AccountAndInformationDTO account = mapIndividualRequestResponseHttpToAccountAndInformationDTO(accountRequestHttp);
        account.setRoles(List.of(RoleNameCommonConstants.INDIVIDUAL));
        account.setPermissions(List.of());

        try {
            accountFacade.createIndividualAccountWithDefaultMethod(account);
            return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
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
        } catch (UrlConfigurationNotFoundException | UnprocessableEntityException | UnexpectedException |
                 ForbiddenException | UnauthorizedException | InternalServerErrorException e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.ERROR_AT_CREATION_ACCOUNT, httpHeaders, HttpStatus.BAD_REQUEST);
        } catch (PasswordPatternNotValidException e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.PASSWORD_PATTERN_NOT_VALID, httpHeaders, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            timeHandler.timingEqualization();
            return new ResponseEntity<>(AccountHttpResponseConstants.ERROR_AT_CREATION_ACCOUNT, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/individual/default-method/activate/token/{token}")
    public ResponseEntity<?> activateCandidateAccountWithDefaultMethod(@PathVariable String token, @RequestParam("email") String email, HttpServletRequest request){
        TimeHandler timeHandler = new TimeHandler(responseTime.responseTimeWithoutDispatching());
        timeHandler.start();

        AccessTokenAndRefreshTokenDTO accessTokenAndRefreshToken;
        HttpHeaders headers = new HttpHeaders();

        try {
            accessTokenAndRefreshToken = accountFacade.activateTheIndividualAccountCreatedByDefaultMethod(new AccountEmailAndActivationTokenDTO(email, token));

            timeHandler.timingEqualization();

            return new ResponseEntity<>(accessTokenAndRefreshToken, headers, HttpStatus.PERMANENT_REDIRECT );
        } catch (EmailNotFoundException | EmailPatternNotValidException | VerificationTokenNotFoundException |
                 VerificationTokenPatternNotValidException | VerificationTokenDurationExpiredException |
                 VerificationTokensNotEqualsException | AccountNotFoundException | AccountIsActivatedException e) {
            timeHandler.timingEqualization();
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    private AccountAndInformationDTO mapIndividualRequestResponseHttpToAccountAndInformationDTO(IndividualRequestResponseHttp individualRequestResponseHttp) {
        AccountAndInformationDTO individual = new AccountAndInformationDTO();
        individual.setIdAccount(individualRequestResponseHttp.getIdAccount());
        individual.setFirstName(individualRequestResponseHttp.getFirstName());
        individual.setLastName(individualRequestResponseHttp.getLastName());
        individual.setEmail(individualRequestResponseHttp.getEmail());
        individual.setBirthDate(individualRequestResponseHttp.getBirthdate());
        individual.setGender(individualRequestResponseHttp.getGender());

        return individual;
    }

    private IndividualRequestResponseHttp mapAccountAndInformationDTOToIndividualRequestResponseHttp(AccountAndInformationDTO individual) {
        IndividualRequestResponseHttp individualRequestResponseHttp = new IndividualRequestResponseHttp();
        individualRequestResponseHttp.setIdAccount(individual.getIdAccount());
        individualRequestResponseHttp.setFirstName(individual.getFirstName());
        individualRequestResponseHttp.setLastName(individual.getLastName());
        individualRequestResponseHttp.setEmail(individual.getEmail());
        individualRequestResponseHttp.setBirthdate(individual.getBirthDate());
        individualRequestResponseHttp.setGender(individual.getGender());

        return individualRequestResponseHttp;
    }

}
