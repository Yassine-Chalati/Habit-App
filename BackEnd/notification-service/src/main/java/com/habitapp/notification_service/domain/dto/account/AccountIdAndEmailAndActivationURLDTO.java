package com.habitapp.notification_service.domain.dto.account;

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
public class AccountIdAndEmailAndActivationURLDTO {
    private long idAccount;
    private String email;
    private String activationURL;
}
