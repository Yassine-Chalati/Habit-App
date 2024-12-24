package com.habitapp.emailing_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmailAndURLDTO {
    private String email;
    private String url;
}
