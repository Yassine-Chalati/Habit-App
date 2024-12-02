package com.habitapp.progress_service.dto.authentication;

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
public class ServiceConnectionInformationDTO {

    private String jti;
    private String ip;
    private LocalDateTime connectionDate;
}
