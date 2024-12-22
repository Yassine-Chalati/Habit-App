package com.habitapp.authentication_service.dto.jwt;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RefreshTokenDTO {
    private String refreshToken;
}
