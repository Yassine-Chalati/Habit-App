package com.habitapp.notification_service.domain.dto.account;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
