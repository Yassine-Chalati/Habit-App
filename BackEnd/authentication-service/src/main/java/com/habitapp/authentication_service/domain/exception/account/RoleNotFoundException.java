package com.habitapp.authentication_service.domain.exception.account;

public class RoleNotFoundException extends Exception {
    public RoleNotFoundException(String message){
        super(message);
    }
}
