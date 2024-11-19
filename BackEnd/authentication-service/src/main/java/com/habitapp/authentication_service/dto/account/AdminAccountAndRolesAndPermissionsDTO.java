package com.menara.authentication.dto.account;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdminAccountAndRolesAndPermissionsDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String registration;
    private List<String> roles;
    private List<String> permissions;
}
