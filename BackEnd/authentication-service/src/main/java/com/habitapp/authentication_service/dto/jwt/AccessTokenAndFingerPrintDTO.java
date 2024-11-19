package com.menara.authentication.dto.jwt;

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
