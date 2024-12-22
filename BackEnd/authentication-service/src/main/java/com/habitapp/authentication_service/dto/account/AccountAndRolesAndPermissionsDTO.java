package com.habitapp.authentication_service.dto.account;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountAndRolesAndPermissionsDTO {
    private String email;
    private String password;
    private List<String> roles;
    private List<String> permissions;
}
