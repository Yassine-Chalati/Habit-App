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
public class CandidateDefaultMethod extends User{
    private String password;
    private final boolean activated;

    CandidateDefaultMethod(long id, String email, Set<GrantedAuthority> roles, Set<GrantedAuthority> permissions, boolean activated) {
        super(id, email, roles, permissions);
        this.activated = activated;
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