package com.menara.authentication.domain.base;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class Authentication {
    private String lastIpConnection;
    private LocalDateTime lastConnection;
    private String userAgent;
    private String screenResolution;
}
