package com.menara.authentication.dto.user.candidate;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CandidateInformationWithoutIdAccountDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String gender;
}
