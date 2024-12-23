package com.habitapp.authentication_service.controller;

import com.habitapp.authentication_service.common.handler.time.TimeHandler;
import com.habitapp.authentication_service.configuration.record.HttpResponseTime;
import com.habitapp.authentication_service.configuration.record.JwtClaim;
import com.habitapp.authentication_service.domain.exception.authentication.AccountActivationException;
import com.habitapp.authentication_service.domain.exception.authentication.AuthenticationTypeNullPointerException;
import com.habitapp.authentication_service.domain.exception.authentication.InstanceOfException;
import com.habitapp.authentication_service.domain.exception.authentication.UnknownAuthenticationTypeException;
import com.habitapp.authentication_service.domain.exception.general.ValueNullException;
import com.habitapp.authentication_service.domain.facade.AuthenticationFacade;
import com.habitapp.authentication_service.dto.account.AccountDTO;
import com.habitapp.authentication_service.dto.jwt.AccessTokenAndRefreshTokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationFacade authenticationFacade;
    private JwtClaim jwtClaim;
    private HttpResponseTime responseTime;

    @PostMapping("/individual/default")
    public ResponseEntity<?> authenticateIndividualWithDefaultMethod(@RequestBody AccountDTO account) {
        TimeHandler timeHandler = new TimeHandler(responseTime.responseTimeWithoutDispatching());
        timeHandler.start();

        AccessTokenAndRefreshTokenDTO accessTokenAndRefreshToken;
        HttpHeaders headers = new HttpHeaders();
        try {
            accessTokenAndRefreshToken = authenticationFacade.authenticateIndividualWithDefaultMethod(account);

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

    @PostMapping("/refresh/token")
    public ResponseEntity<?> authenticateIndividualWithRefreshToken(HttpServletRequest request) {
        TimeHandler timeHandler = new TimeHandler(responseTime.responseTimeWithoutDispatching());
        timeHandler.start();

        AccessTokenAndRefreshTokenDTO accessTokenAndRefreshToken;
        HttpHeaders headers = new HttpHeaders();
        try {
            accessTokenAndRefreshToken = authenticationFacade.authenticateWithRefreshToken(request.getHeader("Refresh-Token"));

            timeHandler.timingEqualization();
            return new ResponseEntity<>(accessTokenAndRefreshToken, headers, HttpStatus.OK);
        } catch (InstanceOfException | ValueNullException e) {
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


}
