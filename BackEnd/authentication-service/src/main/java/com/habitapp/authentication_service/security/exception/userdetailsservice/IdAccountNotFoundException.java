package com.habitapp.authentication_service.security.exception.userdetailsservice;

public class IdAccountNotFoundException extends RuntimeException {
    public IdAccountNotFoundException(String message){
        super(message);
    }
}
