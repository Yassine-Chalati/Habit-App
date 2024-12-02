package com.habitapp.reward_service.domain.entity;

import com.habitapp.reward_service.domain.base.BaseClassName;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class EntityClassName extends BaseClassName {
    @Id
    private long idAccount;
    private String userType;

    // public AccountAuthentication(long idAccount,
    //                              String lastIpConnection,
    //                              LocalDateTime lastConnection,
    //                              String userAgent,
    //                              String screenResolution,
    //                              String userType){
    //     super(lastIpConnection, lastConnection, userAgent, screenResolution);
    //     this.idAccount = idAccount;
    //     this.userType = userType;
    // }
}
