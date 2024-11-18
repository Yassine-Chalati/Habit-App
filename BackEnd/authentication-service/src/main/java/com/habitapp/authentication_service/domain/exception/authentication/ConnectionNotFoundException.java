package com.menara.authentication.domain.exception.authentication;

public class ConnectionNotFoundException extends Exception{
    public ConnectionNotFoundException(String message){
        super(message);
    }
}
