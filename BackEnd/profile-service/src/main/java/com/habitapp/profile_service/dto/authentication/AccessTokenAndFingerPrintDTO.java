package com.habitapp.profile_service.dto.authentication;

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

