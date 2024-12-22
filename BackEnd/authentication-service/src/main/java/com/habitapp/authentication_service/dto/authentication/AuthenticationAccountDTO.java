package com.habitapp.authentication_service.dto.authentication;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthenticationAccountDTO {
    private List<CandidateAuthenticationInformationDTO> candidateAuthenticationInformationList;
    private List<AdminAuthenticationInformationDTO> adminAuthenticationInformationList;
}
