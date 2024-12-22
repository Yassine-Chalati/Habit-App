package com.habitapp.authentication_service.dto.user.individual;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class IndividualInformationWithoutIdAccountDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String gender;
}
