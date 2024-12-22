package com.habitapp.emailing_service.domain.facade;

import java.util.Map;

public interface EmailFacade {
    public boolean sendEmail(String email, String templateHTML, String subject, Map<String, Object> templateVariables);

}
