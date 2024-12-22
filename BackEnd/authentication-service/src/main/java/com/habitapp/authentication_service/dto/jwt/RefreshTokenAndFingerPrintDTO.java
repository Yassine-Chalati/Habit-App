package com.habitapp.authentication_service.dto.jwt;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RefreshTokenAndFingerPrintDTO {
    private String refreshToken;
    private String fingerprint;
}
