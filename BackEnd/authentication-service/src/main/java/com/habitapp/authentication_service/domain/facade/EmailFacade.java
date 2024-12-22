package com.habitapp.authentication_service.domain.facade;

import com.habitapp.authentication_service.annotation.Facade;
import com.habitapp.authentication_service.dto.email.EmailAndUrlDTO;
import com.habitapp.authentication_service.proxy.exception.common.*;

public interface EmailFacade {
    public boolean sendURLActivationAccount(EmailAndUrlDTO emailAndURL) throws UnprocessableEntityException, ForbiddenException, UnauthorizedException, InternalServerErrorException, UnexpectedException;
}
