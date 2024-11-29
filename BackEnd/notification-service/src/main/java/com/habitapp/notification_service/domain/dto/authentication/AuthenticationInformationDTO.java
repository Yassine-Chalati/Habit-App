package com.habitapp.notification_service.domain.dto.authentication;

import java.time.LocalDateTime;

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
public class AuthenticationInformationDTO {
    private long idAccount;
    private String lastIpConnection;
    private LocalDateTime lastConnection;
    private String UserAgent;
    private String screenResolution;
    private String userType;
}
