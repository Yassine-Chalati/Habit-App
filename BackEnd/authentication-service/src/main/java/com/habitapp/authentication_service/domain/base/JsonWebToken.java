package com.menara.authentication.domain.base;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
public class JsonWebToken {
    @Id
    private String jti;
    private Instant expirationDate;
}
