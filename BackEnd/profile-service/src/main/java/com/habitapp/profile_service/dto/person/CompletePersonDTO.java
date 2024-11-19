package com.habitapp.profile_service.dto.person;

import com.habitapp.profile_service.domain.enumeration.Gender;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CompletePersonDTO {
    private Long idAccount;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String imageUrl;
}
