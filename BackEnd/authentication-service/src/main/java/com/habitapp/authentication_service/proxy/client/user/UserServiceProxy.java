package com.menara.authentication.proxy.client.user;

import com.internship_hiring_menara.common.http.request_response.admin.AdminRequestResponseHttp;
import com.internship_hiring_menara.common.http.request_response.admin.CandidateAndAdminRequestResponseHttpList;
import com.internship_hiring_menara.common.http.request_response.candidate.CandidateRequestResponseHttp;
import com.menara.authentication.annotation.Proxy;
import com.menara.authentication.client.user.UserClient;
import com.menara.authentication.dto.user.admin.AdminInformationDTO;
import com.menara.authentication.dto.user.candidate.CandidateInformationDTO;
import com.menara.authentication.dto.user.user.CandidatesAndAdminsInformationDTO;
import com.menara.authentication.proxy.exception.common.UnauthorizedException;
import com.menara.authentication.proxy.exception.common.UnexpectedException;
import com.menara.authentication.proxy.exception.common.UnexpectedResponseBodyException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

@Proxy
@AllArgsConstructor
public class UserServiceProxy {
    private UserClient userClient;

    public CandidatesAndAdminsInformationDTO readAllCandidatesAndAdmins() throws UnexpectedResponseBodyException, UnauthorizedException, UnexpectedException {
        CandidatesAndAdminsInformationDTO candidatesAndAdminsInformation = new CandidatesAndAdminsInformationDTO(new ArrayList<>(), new ArrayList<>());

        try {
            ResponseEntity<CandidateAndAdminRequestResponseHttpList> response = userClient.readAllCandidatesAndAdmins();
            if ((response.getBody() instanceof CandidateAndAdminRequestResponseHttpList  candidatesAndAdmins)){
                if(candidatesAndAdmins.getCandidateRequestResponseHttpList().getCandidateRequestResponseHttpList().isEmpty()
                        && candidatesAndAdmins.getAdminRequestResponseHttpList().getAdminRequestResponseHttpList().isEmpty() ){
                    return candidatesAndAdminsInformation;
                }

                for (CandidateRequestResponseHttp candidate : candidatesAndAdmins.getCandidateRequestResponseHttpList().getCandidateRequestResponseHttpList()) {
                    candidatesAndAdminsInformation.getCandidateInformationList().add(new CandidateInformationDTO(candidate.getIdAccount(), candidate.getFirstName(), candidate.getLastName(), candidate.getEmail(), candidate.getImageProfileUrl(), candidate.getGender()));
                }

                for (AdminRequestResponseHttp admin : candidatesAndAdmins.getAdminRequestResponseHttpList().getAdminRequestResponseHttpList()) {
                    candidatesAndAdminsInformation.getAdminInformationList().add(new AdminInformationDTO(admin.getIdAccount(), admin.getFirstName(), admin.getLastName(), admin.getEmail(), admin.getRegistration()));
                }

                return candidatesAndAdminsInformation;
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
}
