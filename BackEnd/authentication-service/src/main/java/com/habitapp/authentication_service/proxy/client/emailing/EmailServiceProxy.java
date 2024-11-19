package com.menara.authentication.proxy.client.emailing;

import com.internship_hiring_menara.common.http.response.email.EmailAndUrlResponseHttp;
import com.menara.authentication.annotation.Proxy;
import com.menara.authentication.client.emailing.EmailClient;
import com.menara.authentication.dto.email.EmailAndUrlDTO;
import com.menara.authentication.proxy.exception.common.*;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

@Proxy
@AllArgsConstructor
public class EmailServiceProxy {
    private EmailClient emailClient;

    public boolean sendURLActivationAccount(EmailAndUrlDTO emailAndURL) throws UnauthorizedException, ForbiddenException, UnprocessableEntityException, InternalServerErrorException, UnexpectedException {
        try {
            ResponseEntity<?> response = emailClient.sendURLActivationAccount(new EmailAndUrlResponseHttp(emailAndURL.getEmail(), emailAndURL.getUrl()));
            return true;
        } catch (FeignException e){
            switch (e.status()) {
                case 401: throw new UnauthorizedException("unauthorized to send email");
                case 403: throw new ForbiddenException("forbidden to send email");
                case 422: throw new UnprocessableEntityException("emailing service can not processes sending email");
                case 500: throw new InternalServerErrorException("an internal server error at emailing service");
                default: throw  new UnexpectedException("unexpected error at email service");
            }
        }

        /*if (response.getStatusCode() == HttpStatus.OK){
            return true;
        } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED){
            throw new UnauthorizedException("unauthorized to send email");
        } else if (response.getStatusCode() == HttpStatus.FORBIDDEN) {
            throw new ForbiddenException("forbidden to send email");
        } else if (response.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY){
            throw new UnprocessableEntityException("emailing service can not processes sending email");
        } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
            throw new InternalServerErrorException("an internal server error at emailing service");
        } else {
            return false;
        }*/
    }
    public boolean sendURLResetPassword(EmailAndUrlDTO emailAndURL) throws UnauthorizedException, ForbiddenException, UnprocessableEntityException, InternalServerErrorException, UnexpectedException {
        try {
            ResponseEntity<?> response = emailClient.sendURLResetPassword(new EmailAndUrlResponseHttp(emailAndURL.getEmail(), emailAndURL.getUrl()));
            return true;
        } catch (FeignException e){
            switch (e.status()) {
                case 401: throw new UnauthorizedException("unauthorized to send email");
                case 403: throw new ForbiddenException("forbidden to send email");
                case 422: throw new UnprocessableEntityException("emailing service can not processes sending email");
                case 500: throw new InternalServerErrorException("an internal server error at emailing service");
                default: throw  new UnexpectedException("unexpected error at email service");
            }
        }

        /*if (response.getStatusCode() == HttpStatus.OK){
            return true;
        } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED){
            throw new UnauthorizedException("unauthorized to send email");
        } else if (response.getStatusCode() == HttpStatus.FORBIDDEN) {
            throw new ForbiddenException("forbidden to send email");
        } else if (response.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY){
            throw new UnprocessableEntityException("emailing service can not processes sending email");
        } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
            throw new InternalServerErrorException("an internal server error at emailing service");
        } else {
            return false;
        }*/
    }
    public boolean sendInformationOfPasswordReset(String email) throws UnauthorizedException, ForbiddenException, UnprocessableEntityException, InternalServerErrorException, UnexpectedException {
        try {
            ResponseEntity<?> response = emailClient.sendInformationOfPasswordReset(email);
            return true;
        } catch (FeignException e){
            switch (e.status()) {
                case 401: throw new UnauthorizedException("unauthorized to send email");
                case 403: throw new ForbiddenException("forbidden to send email");
                case 422: throw new UnprocessableEntityException("emailing service can not processes sending email");
                case 500: throw new InternalServerErrorException("an internal server error at emailing service");
                default: throw  new UnexpectedException("unexpected error at email service");
            }
        }
    }

}
