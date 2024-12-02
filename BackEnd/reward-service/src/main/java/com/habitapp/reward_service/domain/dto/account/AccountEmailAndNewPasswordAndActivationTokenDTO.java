package com.habitapp.reward_service.domain.dto.account;

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
public class AccountEmailAndNewPasswordAndActivationTokenDTO {
    private String email;
    private String newPassword;
    private String activationToken;
}
