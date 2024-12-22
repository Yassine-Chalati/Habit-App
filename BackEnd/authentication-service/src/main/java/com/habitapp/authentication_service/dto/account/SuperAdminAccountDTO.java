package com.habitapp.authentication_service.dto.account;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SuperAdminAccountDTO {
    private String username;
    private String password;
}
