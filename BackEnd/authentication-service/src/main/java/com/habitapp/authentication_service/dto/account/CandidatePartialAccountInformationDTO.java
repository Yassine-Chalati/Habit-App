package com.habitapp.authentication_service.dto.account;

import lombok.*;

import java.time.LocalDateTime;

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