package com.habitapp.authentication_service.dto.connection;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ServiceConnectionInformationDTO {
    private String idAccount;
    private String jti;
    private LocalDateTime dateExpiration;
    private String ip;
    private LocalDateTime issuedAt;
}
