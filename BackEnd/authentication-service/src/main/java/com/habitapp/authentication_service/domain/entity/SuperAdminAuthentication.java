package com.menara.authentication.domain.entity;

import com.menara.authentication.domain.base.Authentication;
import com.menara.authentication.domain.contract.SuperAdminBelongClass;
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
public class SuperAdminAuthentication extends Authentication implements SuperAdminBelongClass {
    @Id
    private String idAccount;

    public SuperAdminAuthentication(String idAccount, String lastIpConnection, LocalDateTime lastConnection, String userAgent, String screenResolution){
        super(lastIpConnection, lastConnection, userAgent, screenResolution);
        this.idAccount = idAccount;
    }
}
