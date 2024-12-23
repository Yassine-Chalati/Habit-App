package com.habitapp.authentication_service.proxy.client.emailing;


import com.habitapp.authentication_service.annotation.Proxy;
import com.habitapp.authentication_service.client.emailing.EmailClient;
import com.habitapp.authentication_service.dto.email.EmailAndUrlDTO;
import com.habitapp.authentication_service.proxy.exception.common.*;
import com.habitapp.common.http.response.email.EmailAndUrlResponseHttp;
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
    }

}
