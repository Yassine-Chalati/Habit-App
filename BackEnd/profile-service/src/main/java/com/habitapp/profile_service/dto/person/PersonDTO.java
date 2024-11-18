package com.habitapp.profile_service.dto.person;

import com.habitapp.profile_service.domain.enumeration.Gender;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class PersonDTO {
    @NonNull
    private Long idAccount;
    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;
    private Gender gender;
}
