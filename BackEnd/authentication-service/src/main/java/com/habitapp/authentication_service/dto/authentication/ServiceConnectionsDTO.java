package com.habitapp.authentication_service.dto.authentication;

import lombok.*;

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
