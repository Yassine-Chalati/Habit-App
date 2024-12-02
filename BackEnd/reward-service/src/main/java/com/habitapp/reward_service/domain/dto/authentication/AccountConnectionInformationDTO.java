package com.habitapp.reward_service.domain.dto.authentication;

import java.time.LocalDateTime;

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
public class AccountConnectionInformationDTO {
    private String jti;
    private LocalDateTime connectionDate;
    private String userAgent;
    private String ip;
}
