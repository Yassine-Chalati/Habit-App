package com.menara.authentication.domain.entity;

import com.menara.authentication.domain.base.UserJsonWebToken;
import com.menara.authentication.domain.contract.SuperAdminBelongClass;
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
public class SuperAdminJsonWebToken extends UserJsonWebToken implements SuperAdminBelongClass {
    private String idAccount;

    public SuperAdminJsonWebToken(String jti,
                                  Instant expirationDate,
                                  String ip,
                                  Instant issuedAt,
                                  String idAccount,
                                  String userAgent){
        super(jti, expirationDate, ip, issuedAt, userAgent);
        this.idAccount = idAccount;
    }
}
