package com.habitapp.notification_service.domain.dto.authentication;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CandidateAuthenticationInformationDTO {
    @NonNull
    private Long idAccount;
    private String firstName;
    private String lastName;
    @NonNull
    private String email;
    private String imageUrl;
    private String gender;
    private String lastIpConnection;
    private LocalDateTime lastConnection;
    private String UserAgent;
    private String screenResolution;
    private String userType;
}
