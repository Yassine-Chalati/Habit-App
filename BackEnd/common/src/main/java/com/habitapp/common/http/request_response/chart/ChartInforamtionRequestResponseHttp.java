package com.habitapp.common.http.request_response.chart;

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
public class ChartInforamtionRequestResponseHttp {

    private Long idUser;
    private boolean checked;
    private boolean unchecked;
    private boolean newHabit;
    private boolean deleteHabit;
}
