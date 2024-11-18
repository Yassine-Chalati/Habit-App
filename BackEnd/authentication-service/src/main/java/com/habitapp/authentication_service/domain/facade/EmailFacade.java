package com.menara.authentication.domain.facade;

import com.menara.authentication.annotation.Facade;
import com.menara.authentication.dto.email.EmailAndUrlDTO;
import com.menara.authentication.proxy.client.emailing.EmailServiceProxy;
import com.menara.authentication.proxy.exception.common.*;
import lombok.AllArgsConstructor;

public interface EmailFacade {
    public boolean sendURLActivationAccount(EmailAndUrlDTO emailAndURL) throws UnprocessableEntityException, ForbiddenException, UnauthorizedException, InternalServerErrorException, UnexpectedException;
    public boolean sendURLResetPassword(EmailAndUrlDTO emailAndURL) throws UnprocessableEntityException, ForbiddenException, UnauthorizedException, InternalServerErrorException, UnexpectedException;
    public boolean sendInformationOfPasswordReset(String email) throws UnprocessableEntityException, ForbiddenException, UnexpectedException, UnauthorizedException, InternalServerErrorException;
}
