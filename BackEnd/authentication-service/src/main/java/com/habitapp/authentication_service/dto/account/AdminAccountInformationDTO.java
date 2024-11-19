package com.menara.authentication.dto.account;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdminAccountInformationDTO {
    private long idAccount;
    private LocalDateTime creationDate;
    private String email;
    private boolean suspended;
    private String firstName;
    private String lastName;
    private String registration;
    private List<String> roles;
    private List<String> permissions;
}