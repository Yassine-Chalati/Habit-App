package com.habitapp.authentication_service.dto.user.individual;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class IndividualInformationDTO {
    @NonNull
    private Long idAccount;
    private String firstName;
    private String lastName;
    @NonNull
    private String email;
    private String imageUrl;
    private String gender;
}
