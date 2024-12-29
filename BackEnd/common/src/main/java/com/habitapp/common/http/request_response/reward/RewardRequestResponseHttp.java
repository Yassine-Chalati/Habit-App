package com.habitapp.common.http.request_response.reward;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RewardRequestResponseHttp {

    private Long idUser;
    private int streak;
    private String trophy;

}
