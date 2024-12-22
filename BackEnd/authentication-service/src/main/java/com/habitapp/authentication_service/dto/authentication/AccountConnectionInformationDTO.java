package com.habitapp.authentication_service.dto.authentication;

import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountConnectionInformationDTO {
    private String jti;
    private LocalDateTime connectionDate;
    private String userAgent;
    private String ip;
}
