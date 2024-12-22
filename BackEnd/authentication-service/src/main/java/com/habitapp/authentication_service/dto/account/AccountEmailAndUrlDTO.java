package com.habitapp.authentication_service.dto.account;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountEmailAndUrlDTO {
    private String email;
    private String url;
}
