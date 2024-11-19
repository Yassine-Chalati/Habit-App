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
public class UserJsonWebToken extends GeneratedJsonWebToken{
    private String userAgent;

    public UserJsonWebToken(String jti,
                            Instant expirationDate,
                            String ip,
                            Instant issuedAt,
                            String userAgent){
        super(jti, expirationDate, ip, issuedAt);
        this.userAgent = userAgent;
    }
}
