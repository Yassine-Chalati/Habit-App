package com.menara.authentication.dto.jwt;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class JsonWebTokenConnectionInformationDTO {
    private String ip;
    private String screenResolution;
    private String userAgent;
}
