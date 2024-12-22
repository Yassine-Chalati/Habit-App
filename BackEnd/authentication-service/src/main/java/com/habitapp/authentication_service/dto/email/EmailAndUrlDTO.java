package com.habitapp.authentication_service.dto.email;

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
