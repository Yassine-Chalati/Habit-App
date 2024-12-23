package com.habitapp.authentication_service.dto.account;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountIdAndEmailAndActivationURLDTO {
    private long idAccount;
    private String email;
    private String activationURL;
}
