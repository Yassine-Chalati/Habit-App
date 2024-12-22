package com.habitapp.authentication_service.dto.account;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountEmailAndNewPasswordAndActivationTokenDTO {
    private String email;
    private String newPassword;
    private String activationToken;
}
