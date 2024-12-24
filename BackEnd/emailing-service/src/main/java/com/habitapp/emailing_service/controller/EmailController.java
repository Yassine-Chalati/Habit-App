package com.habitapp.emailing_service.controller;

import com.habitapp.emailing_service.commons.constant.EmailSubjectConstants;
import com.habitapp.emailing_service.commons.constant.TemplateHTMLConstants;
import com.habitapp.emailing_service.domain.facade.EmailFacade;
import com.habitapp.emailing_service.dto.EmailAndPDFBase64DTO;
import com.habitapp.emailing_service.dto.EmailAndURLDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/email")
@AllArgsConstructor
public class EmailController {
    private EmailFacade emailFacade;

    @PostMapping("/send-url-activation-account")
    public ResponseEntity<?> sendURLActivationAccount(@RequestBody EmailAndURLDTO emailAndURL){
        System.out.println(emailAndURL.getEmail());
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

    @PostMapping("/send-pdf")
    public ResponseEntity<?> sendPDF(@RequestBody EmailAndPDFBase64DTO emailAndPDFBase64){
        System.out.println(emailAndPDFBase64.getEmail());
        boolean result;
        HttpHeaders headers = new HttpHeaders();


        try {
            result = emailFacade.sendEmailWithPDF(emailAndPDFBase64.getEmail(),  EmailSubjectConstants.SUBJECT_EQUATION_DIFFERENTIAL_PDF, Base64.getDecoder().decode(emailAndPDFBase64.getPdfBase64()), "DifferentialEquation.pdf");
            if(result) {
                return new ResponseEntity<>(headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(headers, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (Exception e){
            return new  ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
