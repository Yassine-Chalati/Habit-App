package com.habitapp.emailing_service.domain.service;

import java.util.Map;

public interface EmailService {
    public boolean sendEmail(String email, String templateHTML, String subject, Map<String, Object> templateVariables);
    public boolean sendEmailWithPDF(String email, String subject, byte[] pdfBytes, String fileName);
}
