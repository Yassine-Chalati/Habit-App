package com.habitapp.habit_service.domain.dto.account;

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
public class AccountAndRolesAndPermissionsDTO {
    private String email;
    private String password;
    private List<String> roles;
    private List<String> permissions;
}
