package com.habitapp.habit_service.domain.dto.account;

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
public class AccountEmailAndActivationTokenDTO {
    private String email;
    private String activationToken;
}