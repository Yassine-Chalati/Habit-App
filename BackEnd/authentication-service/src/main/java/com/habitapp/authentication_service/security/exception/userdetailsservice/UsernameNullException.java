package com.habitapp.authentication_service.security.exception.userdetailsservice;

public class UsernameNullException extends RuntimeException {
    public UsernameNullException(String message){
        super(message);
    }
}
