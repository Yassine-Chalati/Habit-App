package com.menara.authentication.domain.exception.account;

public class RoleNotFoundException extends Exception {
    public RoleNotFoundException(String message){
        super(message);
    }
}
