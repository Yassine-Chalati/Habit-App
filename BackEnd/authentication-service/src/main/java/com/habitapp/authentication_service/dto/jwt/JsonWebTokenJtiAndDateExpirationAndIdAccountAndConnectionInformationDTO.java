package com.menara.authentication.dto.jwt;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class JsonWebTokenJtiAndDateExpirationAndIdAccountAndConnectionInformationDTO {
    private String jti;
    private Instant dateExpiration;
    private String idAccount;
    private String ip;
    private Instant issuedAt;
    private String userAgent;
}
