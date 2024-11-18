package com.menara.authentication.domain.entity;

import com.menara.authentication.domain.base.GeneratedJsonWebToken;
import com.menara.authentication.domain.base.UserJsonWebToken;
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
public class AccountJsonWebToken extends UserJsonWebToken {
    private long idAccount;
    private String userType;

    public AccountJsonWebToken(String jti,
                               Instant expirationDate,
                               long idAccount,
                               String ip,
                               Instant issuedAt,
                               String userAgent,
                               String userType){
        super(jti, expirationDate, ip, issuedAt, userAgent);
        this.idAccount = idAccount;
        this.userType = userType;
    }

}
