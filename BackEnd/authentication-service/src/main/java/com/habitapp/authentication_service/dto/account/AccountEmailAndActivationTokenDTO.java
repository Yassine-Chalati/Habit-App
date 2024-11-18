package com.menara.authentication.dto.account;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountEmailAndActivationTokenDTO {
    private String email;
    private String activationToken;
}
