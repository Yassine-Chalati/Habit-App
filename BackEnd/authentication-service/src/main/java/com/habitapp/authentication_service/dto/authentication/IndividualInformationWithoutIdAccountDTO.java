package com.habitapp.authentication_service.dto.authentication;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class IndividualInformationWithoutIdAccountDTO {
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
