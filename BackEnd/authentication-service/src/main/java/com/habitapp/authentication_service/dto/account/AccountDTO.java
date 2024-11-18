package com.menara.authentication.dto.account;

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
