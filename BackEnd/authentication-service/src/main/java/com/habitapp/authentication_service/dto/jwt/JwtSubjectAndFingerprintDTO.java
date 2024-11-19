package com.menara.authentication.dto.jwt;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class JwtSubjectAndFingerprintDTO {
    private String subject;
    private String fingerprint;
}
