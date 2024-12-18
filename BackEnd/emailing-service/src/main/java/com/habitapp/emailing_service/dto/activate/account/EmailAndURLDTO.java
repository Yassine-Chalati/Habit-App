package com.internship_hiring_menara.emailing_service.dto.activate.account;

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
