package com.habitapp.authentication_service.domain.facade.imp;

import com.habitapp.authentication_service.domain.facade.IndividualFacade;
import com.habitapp.authentication_service.annotation.Facade;
import com.habitapp.authentication_service.proxy.client.profile.IndividualServiceProxy;
import com.habitapp.authentication_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.authentication_service.proxy.exception.common.UnexpectedException;
import lombok.AllArgsConstructor;

@Facade
@AllArgsConstructor
public class CandidateFacadeImp implements IndividualFacade {
    private IndividualServiceProxy individualServiceProxy;

    @Override
    public void createCandidate(CandidateInformationDTO candidateInformationDTO) throws UnexpectedException, UnauthorizedException {
        individualServiceProxy.createCandidate(candidateInformationDTO);
    }
}
