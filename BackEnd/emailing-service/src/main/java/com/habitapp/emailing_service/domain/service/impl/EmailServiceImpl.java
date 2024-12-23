package com.habitapp.emailing_service.domain.service.impl;

import com.habitapp.emailing_service.domain.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@Getter
@Setter
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private JavaMailSender javaMailSender ;
    private TemplateEngine templateEngine;

    @Override
    public boolean sendEmail(String email, String templateHTML, String subject, Map<String, Object> templateVariables) {
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        Context context = new Context();

        context.setVariables(templateVariables);

        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(templateEngine.process(templateHTML, context), true);

            javaMailSender.send(message);
        } catch (MessagingException | MailSendException e) {
            return false;
        }

        return true;
    }
}
