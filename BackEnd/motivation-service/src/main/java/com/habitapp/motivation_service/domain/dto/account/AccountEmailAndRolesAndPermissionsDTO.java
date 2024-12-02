package com.habitapp.motivation_service.domain.dto.account;

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
public class AccountEmailAndRolesAndPermissionsDTO {
    private String email;
    private List<String> roles;
    private List<String> permissions;
}
