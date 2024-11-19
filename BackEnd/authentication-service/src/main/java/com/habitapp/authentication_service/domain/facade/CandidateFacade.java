package com.menara.authentication.domain.facade;

import com.menara.authentication.dto.account.AdminAccountInformationDTO;
import com.menara.authentication.dto.account.CandidatePartialAccountInformationDTO;
import com.menara.authentication.dto.user.candidate.CandidateInformationDTO;
import com.menara.authentication.proxy.exception.common.UnauthorizedException;
import com.menara.authentication.proxy.exception.common.UnexpectedException;
import com.menara.authentication.proxy.exception.common.UnexpectedResponseBodyException;

import java.util.List;

public interface CandidateFacade {
    public void createCandidate(CandidateInformationDTO candidateInformationDTO) throws UnexpectedException, UnauthorizedException;
//    public List<CandidatePartialAccountInformationDTO> readAllCandidates();

}
