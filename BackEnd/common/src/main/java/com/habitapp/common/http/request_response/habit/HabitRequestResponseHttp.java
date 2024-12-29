package com.habitapp.common.http.request_response.habit;

import java.util.Date;
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
public class HabitRequestResponseHttp {

    private Long id;
    private Long idUser;
    private String name;
    private boolean checked;
    private boolean daily;
    private boolean weekly;
    private boolean monthly;
    private Date remainder;
    private List<GoalRequestResponseHttp> goals;
}
