package com.habitapp.authentication_service.dto.account;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountDTO {
    private String email;
    private String password;
}
