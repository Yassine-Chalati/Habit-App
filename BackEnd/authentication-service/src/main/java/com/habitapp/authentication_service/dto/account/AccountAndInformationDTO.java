package com.habitapp.authentication_service.dto.account;

import com.habitapp.profile_service.domain.enumeration.Gender;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountAndInformationDTO {
    private long idAccount;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String email;
    private String password;
    private List<String> roles;
    private List<String> permissions;
}
