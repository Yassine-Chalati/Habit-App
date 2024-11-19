package com.menara.authentication.dto.user.candidate;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class CandidateInformationDTO {
    @NonNull
    private Long idAccount;
    private String firstName;
    private String lastName;
    @NonNull
    private String email;
    private String imageUrl;
    private String gender;
}
