package com.habitapp.authentication_service.domain.facade.imp;

import com.habitapp.authentication_service.annotation.Facade;
import com.habitapp.authentication_service.domain.facade.IndividualFacade;
import com.habitapp.authentication_service.proxy.client.profile.IndividualServiceProxy;
import com.habitapp.authentication_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.authentication_service.proxy.exception.common.UnexpectedException;
import com.habitapp.common.http.request_response.individual.IndividualRequestResponseHttp;
import lombok.AllArgsConstructor;

@Facade
@AllArgsConstructor
public class IndividualFacadeImp implements IndividualFacade {
    private IndividualServiceProxy individualServiceProxy;

    @Override
    public IndividualRequestResponseHttp readIndividualAccountWithDefaultMethod(long idAccount) throws UnexpectedException, UnauthorizedException {
        return individualServiceProxy.readOneIndividual(idAccount);
    }
}
