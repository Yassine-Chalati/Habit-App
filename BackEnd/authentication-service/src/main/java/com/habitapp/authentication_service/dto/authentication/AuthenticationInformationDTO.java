package com.habitapp.authentication_service.dto.authentication;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthenticationInformationDTO {
    private long idAccount;
    private String lastIpConnection;
    private LocalDateTime lastConnection;
    private String UserAgent;
    private String screenResolution;
    private String userType;
}
