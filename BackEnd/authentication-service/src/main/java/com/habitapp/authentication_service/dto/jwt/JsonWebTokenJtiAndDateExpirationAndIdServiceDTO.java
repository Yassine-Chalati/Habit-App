package com.menara.authentication.dto.jwt;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class JsonWebTokenJtiAndDateExpirationAndIdServiceDTO {
    private String jti;
    private Instant dateExpiration;
    private String idService;
    private String ip;
    private Instant issuedAt;

}
