package com.menara.authentication.dto.account;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountEmailAndRolesAndPermissionsDTO {
    private String email;
    private List<String> roles;
    private List<String> permissions;
}
