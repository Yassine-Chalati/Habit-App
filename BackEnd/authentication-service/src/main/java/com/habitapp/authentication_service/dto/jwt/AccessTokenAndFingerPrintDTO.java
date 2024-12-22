package com.habitapp.authentication_service.dto.jwt;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccessTokenAndFingerPrintDTO {
    private String accessToken;
    private String fingerPrint;
}
