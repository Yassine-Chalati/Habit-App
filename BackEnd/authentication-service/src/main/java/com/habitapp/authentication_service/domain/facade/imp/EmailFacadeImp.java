package com.menara.authentication.domain.facade.imp;

import com.menara.authentication.annotation.Facade;
import com.menara.authentication.domain.facade.EmailFacade;
import com.menara.authentication.dto.email.EmailAndUrlDTO;
import com.menara.authentication.proxy.client.emailing.EmailServiceProxy;
import com.menara.authentication.proxy.exception.common.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Facade
public class EmailFacadeImp implements EmailFacade {
    private EmailServiceProxy emailService;
    @Override
    public boolean sendURLActivationAccount(EmailAndUrlDTO emailAndURL) throws UnprocessableEntityException,
            ForbiddenException,
            UnauthorizedException,
            InternalServerErrorException, UnexpectedException {
        return emailService.sendURLActivationAccount(emailAndURL);
    }

    @Override
    public boolean sendURLResetPassword(EmailAndUrlDTO emailAndURL) throws UnprocessableEntityException, ForbiddenException, UnauthorizedException, InternalServerErrorException, UnexpectedException {
        return emailService.sendURLResetPassword(emailAndURL);
    }

    @Override
    public boolean sendInformationOfPasswordReset(String email) throws UnprocessableEntityException, ForbiddenException, UnexpectedException, UnauthorizedException, InternalServerErrorException {
        return emailService.sendInformationOfPasswordReset(email);
    }
}
