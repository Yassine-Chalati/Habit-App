package com.habitapp.common.http.request_response.habit;

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
public class GoalRequestResponseHttp {
    private Long id;
    private String name;
    private Boolean checked;
}
