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
public class CandidateGoogleMethod extends User {
    public CandidateGoogleMethod(long id, String email, Set<GrantedAuthority> roles, Set<GrantedAuthority> permissions) {
        super(id, email, roles, permissions);
    }

}
