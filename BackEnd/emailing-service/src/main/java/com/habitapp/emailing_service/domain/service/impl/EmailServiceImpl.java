package com.habitapp.emailing_service.domain.service.impl;

import com.habitapp.emailing_service.domain.service.EmailService;
import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
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

    @Override
    public boolean sendEmailWithPDF(String email, String subject, byte[] pdfBytes, String fileName) {
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;

        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            ByteArrayDataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
            attachmentPart.setDataHandler(new DataHandler(dataSource));
            attachmentPart.setFileName(fileName);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(attachmentPart);
            message.setContent(multipart);

            javaMailSender.send(message);
        } catch (MessagingException | MailSendException e) {
            return false;
        }

        return true;
    }
}
