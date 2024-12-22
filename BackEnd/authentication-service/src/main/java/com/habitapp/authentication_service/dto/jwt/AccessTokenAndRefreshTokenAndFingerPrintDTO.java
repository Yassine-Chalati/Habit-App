package com.habitapp.authentication_service.dto.jwt;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccessTokenAndRefreshTokenAndFingerPrintDTO {
    private String accessToken;
    private String refreshToken;
    private String fingerPrint;
}
