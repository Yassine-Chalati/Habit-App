package com.habitapp.common.http.request_response.individual;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class IndividualRequestResponseHttp {
    private long idAccount;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private LocalDate birthdate;
    private String password;
}
