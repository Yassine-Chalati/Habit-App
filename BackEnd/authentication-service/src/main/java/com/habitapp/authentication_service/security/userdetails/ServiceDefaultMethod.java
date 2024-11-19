package com.menara.authentication.security.userdetails;

import lombok.*;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class ServiceDefaultMethod implements UserDetails, CredentialsContainer {
    private final String id;
    private final String username;
    private String password;
    private final Set<GrantedAuthority> roles;
    private final Set<GrantedAuthority> permissions;

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.addAll(this.roles);
        grantedAuthorities.addAll(this.permissions);
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public static class ServiceDefaultMethodBuilder{
        public ServiceDefaultMethodBuilder roles(String... roles){
            this.roles = new HashSet<GrantedAuthority>();
            for (String role: roles){
                this.roles.add(new SimpleGrantedAuthority(role));
            }
            return this;
        }

        public ServiceDefaultMethodBuilder permissions(String... permissions){
            this.permissions = new HashSet<GrantedAuthority>();
            for (String permission: permissions){
                this.permissions.add(new SimpleGrantedAuthority(permission));
            }
            return this;
        }
    }
}
