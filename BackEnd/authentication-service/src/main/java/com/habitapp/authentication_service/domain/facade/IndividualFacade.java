package com.habitapp.authentication_service.domain.facade;

import com.habitapp.authentication_service.domain.exception.account.AccountNotFoundException;
import com.habitapp.authentication_service.domain.exception.account.EmailNotFoundException;
import com.habitapp.authentication_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.authentication_service.proxy.exception.common.UnexpectedException;
import com.habitapp.profile_service.domain.entity.Individual;

public interface IndividualFacade {
    public Individual readIndividualAccountWithDefaultMethod(long idAccount) throws EmailNotFoundException, AccountNotFoundException, UnexpectedException, UnauthorizedException;
}
