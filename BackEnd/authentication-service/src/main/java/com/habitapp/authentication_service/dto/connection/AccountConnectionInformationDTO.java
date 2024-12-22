package com.habitapp.authentication_service.dto.connection;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountConnectionInformationDTO {
    private long idAccount;
    private String jti;
    private LocalDateTime dateExpiration;
    private String ip;
    private LocalDateTime issuedAt;
    private String userAgent;

}
