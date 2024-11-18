package com.menara.authentication.dto.jwt;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class JsonWebTokenJtiAndDateExpirationDTO {
    private String jti;
    private Instant dateExpiration;
}
