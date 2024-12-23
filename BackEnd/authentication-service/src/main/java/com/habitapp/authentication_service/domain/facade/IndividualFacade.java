package com.habitapp.authentication_service.domain.facade;

import com.habitapp.authentication_service.domain.exception.account.AccountNotFoundException;
import com.habitapp.authentication_service.domain.exception.account.EmailNotFoundException;
import com.habitapp.authentication_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.authentication_service.proxy.exception.common.UnexpectedException;
import com.habitapp.common.http.request_response.individual.IndividualRequestResponseHttp;


public interface IndividualFacade {
    public IndividualRequestResponseHttp readIndividualAccountWithDefaultMethod(long idAccount) throws EmailNotFoundException, AccountNotFoundException, UnexpectedException, UnauthorizedException;
}
