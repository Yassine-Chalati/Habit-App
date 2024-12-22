package com.habitapp.profile_service.domain.base;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class User {

    @Id
    private long idAccount;
    private String firstName;
    private String lastName;
    private String email;
}
