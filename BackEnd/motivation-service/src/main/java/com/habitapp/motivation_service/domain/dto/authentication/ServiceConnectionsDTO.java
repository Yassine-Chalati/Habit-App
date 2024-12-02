package com.habitapp.motivation_service.domain.dto.authentication;

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
public class ServiceConnectionsDTO {
    private String idService;
    private List<ServiceConnectionInformationDTO> connections;
}
