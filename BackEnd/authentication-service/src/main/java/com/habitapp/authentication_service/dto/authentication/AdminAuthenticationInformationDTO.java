package com.habitapp.authentication_service.dto.authentication;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdminAuthenticationInformationDTO {
    @NonNull
    private Long idAccount;
    private String firstName;
    private String lastName;
    @NonNull
    private String email;
    private String registration;
    private String lastIpConnection;
    private LocalDateTime lastConnection;
    private String UserAgent;
    private String screenResolution;
    private String userType;
}
