package com.menara.authentication.domain.entity;

import com.menara.authentication.domain.base.GeneratedJsonWebToken;
import com.menara.authentication.domain.contract.ServiceBelongClass;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class ServiceJsonWebToken extends GeneratedJsonWebToken implements ServiceBelongClass {
    private String idService;

    public ServiceJsonWebToken(String jti,
                               Instant expirationDate,
                               String ip,
                               Instant issuedAt,
                               String idService){
        super(jti, expirationDate, ip, issuedAt);
        this.idService = idService;
    }
}
