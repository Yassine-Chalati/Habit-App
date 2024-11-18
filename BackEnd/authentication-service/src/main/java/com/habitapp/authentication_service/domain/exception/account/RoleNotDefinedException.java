package com.menara.authentication.domain.exception.account;

public class RoleNotDefinedException extends Exception {
    public RoleNotDefinedException(String message){
        super(message);
    }
}
