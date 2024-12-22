package com.habitapp.authentication_service.domain.exception.account;

public class RoleNotDefinedException extends Exception {
    public RoleNotDefinedException(String message){
        super(message);
    }
}
