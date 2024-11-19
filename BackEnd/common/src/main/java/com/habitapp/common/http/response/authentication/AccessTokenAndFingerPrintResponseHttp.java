package com.habitapp.common.http.response.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccessTokenAndFingerPrintResponseHttp {
    private String accessToken;
    private String fingerPrint;
}
