package com.habitapp.authentication_service.dto.account;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdminAccountWithPasswordDTO {
    private long idAccount;
    private LocalDateTime creationDate;
    private String email;
    private String password;
    private boolean suspended;
    private List<String> roles;
    private List<String> permissions;
}
