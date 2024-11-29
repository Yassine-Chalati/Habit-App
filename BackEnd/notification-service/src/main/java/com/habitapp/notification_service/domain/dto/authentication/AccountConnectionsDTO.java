package com.habitapp.notification_service.domain.dto.authentication;

import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;

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
public class AccountConnectionsDTO {
    private long idAccount;
    private String email;
    private String firstName;
    private String lastName;
    @NonNull
    private String gender;
    @NotNull
    private String imageProfileUrl;
    private List<AccountConnectionInformationDTO> connections;
}
