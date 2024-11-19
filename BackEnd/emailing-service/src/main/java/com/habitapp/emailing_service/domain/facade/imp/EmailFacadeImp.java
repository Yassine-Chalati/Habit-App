package com.internship_hiring_menara.emailing_service.domain.facade.imp;

import com.internship_hiring_menara.emailing_service.annotation.Facade;
import com.internship_hiring_menara.emailing_service.domain.facade.EmailFacade;
import com.internship_hiring_menara.emailing_service.domain.service.EmailService;
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
}
