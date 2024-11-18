package com.habitapp.notification_service.dto.authentication;

import lombok.*;

import java.time.LocalDateTime;
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
