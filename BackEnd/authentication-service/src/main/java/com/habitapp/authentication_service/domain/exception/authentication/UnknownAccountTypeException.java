package com.habitapp.authentication_service.domain.exception.authentication;

public class UnknownAccountTypeException extends Exception {
    public UnknownAccountTypeException(String message){
        super(message);
    }
}
