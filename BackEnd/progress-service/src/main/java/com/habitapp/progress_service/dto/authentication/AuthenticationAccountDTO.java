package com.habitapp.progress_service.dto.authentication;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthenticationAccountDTO {

    private List<CandidateAuthenticationInformationDTO> candidateAuthenticationInformationList;
    private List<AdminAuthenticationInformationDTO> adminAuthenticationInformationList;
}
