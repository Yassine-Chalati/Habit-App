package com.habitapp.authentication_service.dto.account;

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
    private String gender;
    private String email;
    private String password;
    private List<String> roles;
    private List<String> permissions;
}
