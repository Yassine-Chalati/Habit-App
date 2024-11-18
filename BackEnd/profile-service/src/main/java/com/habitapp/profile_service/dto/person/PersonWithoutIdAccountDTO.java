package com.habitapp.profile_service.dto.person;

import com.habitapp.profile_service.domain.enumeration.Gender;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class PersonWithoutIdAccountDTO {
    @NonNull
    private Long idAccount;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String gender;
}
