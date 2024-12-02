package com.habitapp.progress_service.dto.account;

import java.time.LocalDateTime;
import java.util.List;

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
public class AdminAccountWithPasswordDTO {

    private long idAccount;
    private LocalDateTime creationDate;
    private String email;
    private String password;
    private boolean suspended;
    private List<String> roles;
    private List<String> permissions;
}
