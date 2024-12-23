package com.habitapp.authentication_service.security.userdetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
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
@SuperBuilder
public class User implements UserDetails, CredentialsContainer {
    private final long id;
    private final String email;
    private final Set<GrantedAuthority> roles;
    private final Set<GrantedAuthority> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.addAll(this.roles);
        grantedAuthorities.addAll(this.permissions);
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return "{noop}withoutPassword";
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public void eraseCredentials() {
    }



    public static abstract class UserBuilder<C extends User, B extends UserBuilder<C, B>>{
        public B roles(String[] roles){
            this.roles = new HashSet<GrantedAuthority>();
            for (String role: roles){
                this.roles.add(new SimpleGrantedAuthority(role));
            }
            return (B) this;
        }

        public B permissions(String[] permissions){
            this.permissions = new HashSet<GrantedAuthority>();
            for (String permission: permissions){
                this.permissions.add(new SimpleGrantedAuthority(permission));
            }
            return (B) this;
        }
    }


}
