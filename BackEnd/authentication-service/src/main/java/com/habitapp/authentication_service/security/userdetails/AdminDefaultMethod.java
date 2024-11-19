package com.menara.authentication.security.userdetails;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@SuperBuilder
@Getter
@Setter
@ToString
public class AdminDefaultMethod extends User {
    private final boolean suspended;
    private String password;


    AdminDefaultMethod(long id, String email, Set<GrantedAuthority> roles, Set<GrantedAuthority> permissions, boolean suspended) {
        super(id, email, roles, permissions);
        this.suspended = suspended;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
