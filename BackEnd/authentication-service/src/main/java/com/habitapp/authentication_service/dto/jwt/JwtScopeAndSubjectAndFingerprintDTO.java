package com.menara.authentication.dto.jwt;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class JwtScopeAndSubjectAndFingerprintDTO {
    private String subject;
    private String scope;
    private String fingerprint;
}
