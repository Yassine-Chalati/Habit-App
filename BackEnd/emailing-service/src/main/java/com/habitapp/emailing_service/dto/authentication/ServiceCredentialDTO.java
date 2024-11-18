package com.internship_hiring_menara.emailing_service.dto.authentication;

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