package com.habitapp.motivation_service.domain.dto.account;

import java.time.LocalDateTime;

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
public class CandidatePartialAccountInformationDTO {
    private long idAccount;
    private LocalDateTime creationDate;
    private String email;
    private boolean activated;
    private String firstName;
    private String lastName;
    private String imageProfile;
    private String gender;
}