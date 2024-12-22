package com.habitapp.authentication_service.domain.facade;

import com.habitapp.authentication_service.dto.user.individual.IndividualInformationDTO;
import com.habitapp.authentication_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.authentication_service.proxy.exception.common.UnexpectedException;

public interface IndividualFacade {
    public void createIndividual(IndividualInformationDTO candidateInformationDTO) throws UnexpectedException, UnauthorizedException;

}
