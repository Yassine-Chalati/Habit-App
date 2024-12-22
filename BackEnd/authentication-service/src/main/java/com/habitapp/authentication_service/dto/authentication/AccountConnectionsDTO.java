package com.habitapp.authentication_service.dto.authentication;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

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
