package com.menara.authentication.domain.entity;

import com.menara.authentication.domain.base.GoogleAccount;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GoogleAccountCandidate extends GoogleAccount {
    public GoogleAccountCandidate(String email, LocalDateTime creationDate, List<Role> roles, List<Permission> permissions){
        super(email, creationDate, roles, permissions);
    }
}
