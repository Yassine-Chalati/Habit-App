package com.habitapp.authentication_service.dto.user.admin;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminInformationDTO {
    @NonNull
    private Long idAccount;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String email;
    @NonNull
    private String registration;
}
