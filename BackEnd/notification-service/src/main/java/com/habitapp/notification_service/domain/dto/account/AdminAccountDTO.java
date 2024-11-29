package com.habitapp.notification_service.domain.dto.account;

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
public class AdminAccountDTO {
    private long idAccount;
    private LocalDateTime creationDate;
    private String email;
    private boolean suspended;
    private List<String> roles;
    private List<String> permissions;
}
