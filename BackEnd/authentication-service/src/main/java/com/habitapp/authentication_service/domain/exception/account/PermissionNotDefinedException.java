package com.menara.authentication.domain.exception.account;

public class PermissionNotDefinedException extends Exception{
    public PermissionNotDefinedException(String message){
        super(message);
    }
}
