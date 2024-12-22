package com.habitapp.authentication_service.domain.exception.account;

public class PermissionNotDefinedException extends Exception{
    public PermissionNotDefinedException(String message){
        super(message);
    }
}
