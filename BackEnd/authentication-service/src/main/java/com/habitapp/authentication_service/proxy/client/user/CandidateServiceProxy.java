package com.menara.authentication.proxy.client.user;

import com.internship_hiring_menara.common.http.request.candidate.CandidateRequestHttp;
import com.internship_hiring_menara.common.http.request_response.candidate.CandidateRequestResponseHttp;
import com.internship_hiring_menara.common.http.request_response.candidate.CandidateRequestResponseHttpList;
import com.menara.authentication.annotation.Proxy;
import com.menara.authentication.client.user.CandidateClient;
import com.menara.authentication.dto.user.candidate.CandidateInformationDTO;
import com.menara.authentication.proxy.exception.common.*;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Proxy
@AllArgsConstructor
public class CandidateServiceProxy {
    private CandidateClient candidateClient;

    public void createCandidate(CandidateInformationDTO candidateInformationDTO) throws UnauthorizedException, UnexpectedException {
        CandidateRequestHttp candidateRequestHttp = new CandidateRequestHttp(candidateInformationDTO.getIdAccount(), candidateInformationDTO.getEmail());
//        ResponseEntity<?> response;

        System.out.println(candidateInformationDTO.getIdAccount());

        candidateRequestHttp.setIdAccount(candidateInformationDTO.getIdAccount());
        candidateRequestHttp.setFirstName(candidateInformationDTO.getFirstName());
        candidateRequestHttp.setLastName(candidateInformationDTO.getLastName());
        candidateRequestHttp.setImageUrl(candidateInformationDTO.getImageUrl());
        candidateRequestHttp.setGender(candidateInformationDTO.getGender());
        try {
            candidateClient.createCandidate(candidateRequestHttp);
//            response = candidateClient.createCandidate(candidateRequestHttp);
//            response.getStatusCode();
        } catch (FeignException e){
            if (e.status() == 401) {
                throw new UnauthorizedException("unauthorized to create candidate");
            }
            throw new UnexpectedException("unexpected error at user service httpstatus : " + e.status());
        }
    }

    public Map<Long, CandidateInformationDTO> readAllCandidates() throws UnexpectedResponseBodyException, UnauthorizedException, UnexpectedException {
        Map<Long, CandidateInformationDTO> candidatesMap = new HashMap<>();

        try {
            ResponseEntity<CandidateRequestResponseHttpList> response = candidateClient.readAllCandidate();
            if ((response.getBody() instanceof CandidateRequestResponseHttpList  candidates)){
                if(candidates.getCandidateRequestResponseHttpList().isEmpty() ){
                    return candidatesMap;
                }

                for (CandidateRequestResponseHttp candidate : candidates.getCandidateRequestResponseHttpList()) {
                    candidatesMap.put(candidate.getIdAccount(), new CandidateInformationDTO(candidate.getIdAccount(), candidate.getFirstName(), candidate.getLastName(), candidate.getEmail(), candidate.getImageProfileUrl(), candidate.getGender()));
                }
                return candidatesMap;
            } else {
                throw new UnexpectedResponseBodyException("unexpected response body rather than AdminRequestResponseHttpList ");
            }
        } catch (FeignException e){
            if (e.status() == 401) {
                throw new UnauthorizedException("unauthorized to read all admin from user-service");
            }
            System.out.println(e.getMessage());
            throw new UnexpectedException("unexpected error at user service http status : " + e.status());
        }
    }


   /* private CandidateService candidateService;

    public NewCandidateCommonDTO createNewCandidate(long idAccount) throws IdAccountNotFoundException, RoleNotFoundException, RolePrefixException, RoleNotDefinedException, CandidateNotCreatedException, UnexpectedException {
        ResponseEntity<Object> response = candidateService.createNewCandidate(idAccount);
        if(response.getStatusCode() == HttpStatus.CREATED){
            return (NewCandidateCommonDTO) response.getBody();
        }

        if (response.getStatusCode() == HttpStatus.BAD_REQUEST){
            if (response.getBody() instanceof CandidateErrorHttpResponseCommonDTO error){

                if (error.getCode() == CandidateHttpResponseCommonConstants.ID_ACCOUNT.getCode())
                    throw new IdAccountNotFoundException(error.getMessage());
                else if (error.getCode() == CandidateHttpResponseCommonConstants.ROLE_PREFIX.getCode())
                    throw new RolePrefixException(error.getMessage());
                else if (error.getCode() == CandidateHttpResponseCommonConstants.ROLE_NOT_DEFINED.getCode())
                    throw new RoleNotDefinedException(error.getMessage());
                else if (error.getCode() == CandidateHttpResponseCommonConstants.CANDIDATE_NOT_CREATED.getCode())
                    throw new CandidateNotCreatedException(error.getMessage());
                else if (error.getCode() == CandidateHttpResponseCommonConstants.ROLE_NOT_FOUND.getCode())
                    throw new RoleNotFoundException(error.getMessage());
            } else
                throw new UnexpectedException("unexpected error in http status bad request");
        }

        if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
            throw new UnexpectedException((String) response.getBody());
        }

        return null ;
    }*/
}
