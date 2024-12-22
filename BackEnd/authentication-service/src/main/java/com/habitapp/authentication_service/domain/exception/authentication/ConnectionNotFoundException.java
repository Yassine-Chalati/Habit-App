package com.habitapp.authentication_service.domain.exception.authentication;

public class ConnectionNotFoundException extends Exception{
    public ConnectionNotFoundException(String message){
        super(message);
    }
}
