package com.habitapp.authentication_service.domain.base;

import com.habitapp.authentication_service.domain.entity.Permission;
import com.habitapp.authentication_service.domain.entity.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountSeq")
    @SequenceGenerator(name = "accountSeq", initialValue = 1)
    private long id;
    private String email;
    private LocalDateTime creationDate;
    @ManyToMany
    private List<Role> roles;
    @ManyToMany
    private List<Permission> permissions;

    public Account(String email, LocalDateTime creationDate, List<Role> roles, List<Permission> permissions){
        this.email = email;
        this.creationDate = creationDate;
        this.roles = roles;
        this.permissions = permissions;
    }

    public Account(long id, String email, LocalDateTime creationDate){
        this.email = email;
        this.creationDate = creationDate;
        this.roles = null;
        this.permissions = null;
    }
}
