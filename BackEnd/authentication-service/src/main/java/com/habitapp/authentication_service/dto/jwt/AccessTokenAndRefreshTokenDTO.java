package com.menara.authentication.dto.jwt;

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
