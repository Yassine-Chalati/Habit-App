package com.menara.authentication.dto.email;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmailAndUrlDTO {
    private String email;
    private String url;
}
