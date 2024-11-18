package com.menara.authentication.domain.base;

import com.menara.authentication.domain.base.Account;
import com.menara.authentication.domain.entity.Permission;
import com.menara.authentication.domain.entity.Role;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class GoogleAccount extends Account {

    public GoogleAccount(String email, LocalDateTime creationDate, List<Role> roles, List<Permission> permissions){
        super(email, creationDate, roles, permissions);
    }
}
