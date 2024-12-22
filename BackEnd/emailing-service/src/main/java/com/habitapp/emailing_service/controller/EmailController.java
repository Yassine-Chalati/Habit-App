package com.habitapp.emailing_service.controller;

import com.habitapp.emailing_service.commons.constant.EmailSubjectConstants;
import com.habitapp.emailing_service.commons.constant.TemplateHTMLConstants;
import com.habitapp.emailing_service.domain.facade.EmailFacade;
import com.habitapp.emailing_service.dto.activate.account.EmailAndURLDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/email")
@AllArgsConstructor
public class EmailController {
    private EmailFacade emailFacade;

    @PostMapping("/send-url-activation-account")
    public ResponseEntity<?> sendURLActivationAccount(@RequestBody EmailAndURLDTO emailAndURL){
        Map<String, Object> templateVariables = new HashMap<>();
        boolean result;
        HttpHeaders headers = new HttpHeaders();

        templateVariables.put("urlActivation", emailAndURL.getUrl());
        templateVariables.put("emailAddress", emailAndURL.getEmail());
        try {
            result = emailFacade.sendEmail(emailAndURL.getEmail(), TemplateHTMLConstants.TEMPLATE_ACTIVATION_ACCOUNT, EmailSubjectConstants.SUBJECT_ACTIVATION_ACCOUNT, templateVariables);
            if(result) {
                return new ResponseEntity<>(headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(headers, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (Exception e){
            return new  ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/send-url-reset-password-account")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).AUTHENTICATION_SERVER)")
    public ResponseEntity<?> sendURLResetPasswordAccount(@RequestBody EmailAndURLDTO emailAndURL){
        Map<String, Object> templateVariables = new HashMap<>();
        boolean result;
        HttpHeaders headers = new HttpHeaders();

        templateVariables.put("urlResetPassword", emailAndURL.getUrl());
        templateVariables.put("emailAddress", emailAndURL.getEmail());
        try {
            result = emailFacade.sendEmail(emailAndURL.getEmail(), TemplateHTMLConstants.TEMPLATE_RESET_PASSWORD_ACCOUNT, EmailSubjectConstants.SUBJECT_RESET_PASSWORD_ACCOUNT, templateVariables);
            if(result) {
                return new ResponseEntity<>(headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(headers, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (Exception e){
            return new  ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/send-information-of-password-reset")
    @PreAuthorize("hasRole(T(com.internship_hiring_menara.common.common.account.RoleNameCommonConstants).AUTHENTICATION_SERVER)")
    public ResponseEntity<?> sendInformationOfPasswordReset(@RequestBody String email){
        Map<String, Object> templateVariables = new HashMap<>();
        boolean result;
        HttpHeaders headers = new HttpHeaders();

        templateVariables.put("emailAddress", email);
        try {
            result = emailFacade.sendEmail(email, TemplateHTMLConstants.TEMPLATE_INFORM_PASSWORD_RESET, EmailSubjectConstants.SUBJECT_INFORM_PASSWORD_RESET, templateVariables);
            if(result) {
                return new ResponseEntity<>(headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(headers, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (Exception e){
            return new  ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


   /* @GetMapping("/send-url-activation-account")
    public String send(@RequestBody EmailAndURLDTO emailAndURL) {
        return emailAndURL.getEmail();
    }*/
}
