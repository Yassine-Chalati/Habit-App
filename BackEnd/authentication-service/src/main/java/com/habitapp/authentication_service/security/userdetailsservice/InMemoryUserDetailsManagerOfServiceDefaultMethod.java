package com.menara.authentication.security.userdetailsservice;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.menara.authentication.security.userdetails.ServiceDefaultMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.log.LogMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.memory.UserAttribute;
import org.springframework.security.core.userdetails.memory.UserAttributeEditor;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.util.Assert;

@Setter
@Getter
@NoArgsConstructor
public class InMemoryUserDetailsManagerOfServiceDefaultMethod implements UserDetailsService {
    private final Map<String, ServiceDefaultMethod> services = new HashMap<>();

    public InMemoryUserDetailsManagerOfServiceDefaultMethod(Collection<ServiceDefaultMethod> services) {
        for (ServiceDefaultMethod service : services) {
            createService(service);
        }
    }

    public InMemoryUserDetailsManagerOfServiceDefaultMethod(ServiceDefaultMethod... users) {
        for (ServiceDefaultMethod user : users) {
            createService(user);
        }
    }

    private User createUserDetails(String name, UserAttribute attr) {
        return new User(name, attr.getPassword(), attr.isEnabled(), true, true, true, attr.getAuthorities());
    }

    public void createService(ServiceDefaultMethod service) {
        Assert.isTrue(!serviceExists(service.getUsername()), "user should not exist");
        this.services.put(service.getUsername().toLowerCase(), service);
    }

    public void deleteUser(String username) {
        this.services.remove(username.toLowerCase());
    }

    public void updateUser(ServiceDefaultMethod service) {
        Assert.isTrue(serviceExists(service.getUsername()), "user should exist");
        this.services.put(service.getUsername().toLowerCase(), service);
    }

    public boolean serviceExists(String username) {
        return this.services.containsKey(username.toLowerCase());
    }

    @Override
    public ServiceDefaultMethod loadUserByUsername(String username) throws UsernameNotFoundException {
        ServiceDefaultMethod service = this.services.get(username.toLowerCase());
        if (service == null) {
            throw new UsernameNotFoundException(username);
        }

        System.out.println("password : " + service.getPassword());
        return new ServiceDefaultMethod(service.getId(),
                service.getUsername(),
                service.getPassword(),
                service.getRoles(),
                service.getPermissions());
    }

}
