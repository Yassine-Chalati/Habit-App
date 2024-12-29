package com.habitapp.common.http.request_response.quote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class QuoteRequestResponseHttp {

    private long id;
    private String message;
    private String weekDay;

}
