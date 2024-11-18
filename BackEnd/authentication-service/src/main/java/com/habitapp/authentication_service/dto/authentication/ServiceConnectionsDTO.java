package com.menara.authentication.dto.authentication;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ServiceConnectionsDTO {
    private String idService;
    private List<ServiceConnectionInformationDTO> connections;
}
