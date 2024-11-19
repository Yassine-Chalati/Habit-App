package com.menara.authentication.dto.user.user;

import com.menara.authentication.dto.user.admin.AdminInformationDTO;
import com.menara.authentication.dto.user.candidate.CandidateInformationDTO;
import lombok.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CandidatesAndAdminsInformationDTO {
    private List<CandidateInformationDTO> candidateInformationList;
    private List<AdminInformationDTO> adminInformationList;
}
