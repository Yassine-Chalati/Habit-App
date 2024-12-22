package com.habitapp.authentication_service.dto.user.user;

import com.habitapp.authentication_service.dto.user.admin.AdminInformationDTO;
import com.habitapp.authentication_service.dto.user.individual.CandidateInformationDTO;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CandidatesAndAdminsInformationDTO {
    private List<CandidateInformationDTO> candidateInformationList;
    private List<AdminInformationDTO> adminInformationList;
}
