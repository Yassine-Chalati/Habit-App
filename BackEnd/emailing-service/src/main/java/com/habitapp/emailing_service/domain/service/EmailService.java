package com.internship_hiring_menara.emailing_service.domain.service;

import java.util.Map;

public interface EmailService {
    public boolean sendEmail(String email, String templateHTML, String subject, Map<String, Object> templateVariables);
}
