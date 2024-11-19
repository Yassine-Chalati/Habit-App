package com.menara.authentication.domain.entity;

import com.menara.authentication.domain.base.JsonWebToken;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class RevokedJsonWebToken extends JsonWebToken {

    public RevokedJsonWebToken(String jti, Instant expirationDate){
        super(jti, expirationDate);
    }
}
