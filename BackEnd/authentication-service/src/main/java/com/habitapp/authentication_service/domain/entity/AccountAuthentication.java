package com.menara.authentication.domain.entity;

import com.menara.authentication.domain.base.Authentication;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class AccountAuthentication extends Authentication {
    @Id
    private long idAccount;
    private String userType;

    public AccountAuthentication(long idAccount,
                                 String lastIpConnection,
                                 LocalDateTime lastConnection,
                                 String userAgent,
                                 String screenResolution,
                                 String userType){
        super(lastIpConnection, lastConnection, userAgent, screenResolution);
        this.idAccount = idAccount;
        this.userType = userType;
    }
}
