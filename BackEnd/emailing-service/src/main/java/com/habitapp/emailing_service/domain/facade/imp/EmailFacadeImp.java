package com.habitapp.emailing_service.domain.facade.imp;

import com.habitapp.emailing_service.annotation.Facade;
import com.habitapp.emailing_service.domain.facade.EmailFacade;
import com.habitapp.emailing_service.domain.service.EmailService;
import lombok.AllArgsConstructor;

import java.util.Map;

@Facade
@AllArgsConstructor
public class EmailFacadeImp implements EmailFacade {
    private EmailService emailService;

    @Override
    public boolean sendEmail(String email, String templateHTML, String subject, Map<String, Object> templateVariables) {
        return emailService.sendEmail(email, templateHTML, subject, templateVariables);
    }

    @Override
    public boolean sendEmailWithPDF(String email, String subject, byte[] pdfBytes, String fileName) {
        return emailService.sendEmailWithPDF(email, subject, pdfBytes, fileName);
    }
}
