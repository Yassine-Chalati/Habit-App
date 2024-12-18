package com.menara.authentication.dto.authentication;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ServiceConnectionInformationDTO {
    private String jti;
    private String ip;
    private LocalDateTime connectionDate;
}
