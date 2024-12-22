package com.habitapp.authentication_service.domain.base;

import com.habitapp.authentication_service.domain.entity.Permission;
import com.habitapp.authentication_service.domain.entity.Role;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class DefaultAccount extends Account{
    private String password;

    public DefaultAccount(String email, String password, LocalDateTime creationDate, List<Role> roles, List<Permission> permissions){
        super(email, creationDate, roles, permissions);
        this.password = password;
    }

    public DefaultAccount(long id, String email, String password, LocalDateTime creationDate){
        super(id, email, creationDate);
        this.password = password;
    }
}
