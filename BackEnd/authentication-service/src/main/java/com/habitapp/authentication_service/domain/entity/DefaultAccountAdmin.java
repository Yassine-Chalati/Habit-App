package com.menara.authentication.domain.entity;

import com.menara.authentication.domain.base.DefaultAccount;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DefaultAccountAdmin extends DefaultAccount {
    private boolean suspended;

    public DefaultAccountAdmin(String email, String password, LocalDateTime creationDate, List<Role> roles, List<Permission> permissions){
        super(email, password, creationDate, roles, permissions);
        this.suspended = false;
    }
}
