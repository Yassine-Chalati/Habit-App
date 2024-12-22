package com.habitapp.authentication_service.dto.service;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ServiceCredentialDTO {
    private String username;
    private String password;
}
