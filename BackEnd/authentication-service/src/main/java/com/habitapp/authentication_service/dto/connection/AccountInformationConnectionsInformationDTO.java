package com.habitapp.authentication_service.dto.connection;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountInformationConnectionsInformationDTO {
    private Long idAccount;
    private String firstName;
    private String lastName;
    private String email;
    private List<AccountConnectionInformationDTO> ConnectionsInformationList;
}
