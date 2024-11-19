package com.habitapp.common.http.request_response.person;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PersonRequestResponseHttp {
    @NonNull
    private Long idAccount;
    private String firstName;
    private String lastName;
    private String email;
    private String imageProfileUrl;
    private String gender;
}
