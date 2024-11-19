package com.menara.authentication.dto.connection;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SuperAdminConnectionInformationDTO {
    private String idAccount;
    private String jti;
    private LocalDateTime dateExpiration;
    private String ip;
    private LocalDateTime issuedAt;
    private String userAgent;
}
