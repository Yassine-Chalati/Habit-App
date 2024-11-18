package com.menara.authentication.domain.base;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class GeneratedJsonWebToken extends JsonWebToken {
    private String ip;
    private Instant issuedAt;

    public GeneratedJsonWebToken(String jti,
                                 Instant expirationDate,
                                 String ip,
                                 Instant issuedAt){
        super(jti, expirationDate);
        this.ip = ip;
        this.issuedAt = issuedAt;
    }
}
