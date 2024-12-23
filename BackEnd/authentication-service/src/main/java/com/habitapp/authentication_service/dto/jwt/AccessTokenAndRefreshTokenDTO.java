package com.habitapp.authentication_service.dto.jwt;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccessTokenAndRefreshTokenDTO {
    private String accessToken;
    private String refreshToken;
}
