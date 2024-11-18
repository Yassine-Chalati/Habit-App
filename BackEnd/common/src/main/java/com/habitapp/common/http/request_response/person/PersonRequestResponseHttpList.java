package com.habitapp.common.http.request_response.person;

import java.util.List;

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
public class PersonRequestResponseHttpList {
    private List<PersonRequestResponseHttp> personRequestResponseHttpList;
}
