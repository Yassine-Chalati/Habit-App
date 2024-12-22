package com.habitapp.authentication_service.domain.exception.authentication;

public class BadCredentialsException extends Exception{
    public BadCredentialsException(String message){
        super(message);
    }
}
