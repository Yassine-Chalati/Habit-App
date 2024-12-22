package com.habitapp.authentication_service.domain.exception.authentication;

public class UnknownAuthenticationTypeException extends Exception {
    public UnknownAuthenticationTypeException(String message){
        super(message);
    }
}
