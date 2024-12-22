package com.habitapp.authentication_service.security.exception.userdetailsservice;

public class PasswordNullException extends RuntimeException{

    public PasswordNullException(String message){
        super(message);
    }
}
