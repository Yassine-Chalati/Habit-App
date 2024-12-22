package com.habitapp.authentication_service.dto.account;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountIdAndAuthoritiesDTO {
    private String idAccount;
    private String authorities;
}
