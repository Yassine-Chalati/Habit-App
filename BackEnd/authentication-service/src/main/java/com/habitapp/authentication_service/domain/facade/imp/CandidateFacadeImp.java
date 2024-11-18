package com.menara.authentication.domain.facade.imp;

import com.menara.authentication.annotation.Facade;
import com.menara.authentication.domain.facade.CandidateFacade;
import com.menara.authentication.dto.user.candidate.CandidateInformationDTO;
import com.menara.authentication.proxy.client.user.CandidateServiceProxy;
import com.menara.authentication.proxy.exception.common.UnauthorizedException;
import com.menara.authentication.proxy.exception.common.UnexpectedException;
import lombok.AllArgsConstructor;

@Facade
@AllArgsConstructor
public class CandidateFacadeImp implements CandidateFacade {
    private CandidateServiceProxy candidateServiceProxy;

    @Override
    public void createCandidate(CandidateInformationDTO candidateInformationDTO) throws UnexpectedException, UnauthorizedException {
        candidateServiceProxy.createCandidate(candidateInformationDTO);
    }
}
